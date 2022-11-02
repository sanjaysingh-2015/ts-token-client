package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.rule.*;
import com.techsophy.tstokens.entity.Counter;
import com.techsophy.tstokens.entity.CounterProcessMapping;
import com.techsophy.tstokens.entity.ProcessStage;
import com.techsophy.tstokens.exception.InvalidDataException;
import com.techsophy.tstokens.exception.InvalidOperationException;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CounterProcessStageMappingService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final CounterRepository counterRepository;
    private final ProcessStageRepository processStageRepository;
    private final CounterProcessMappingRepository counterProcessMappingRepository;

    private static final String CREATED = "ACTIVE";
    private static final String DELETED = "DELETED";

    @Autowired
    public CounterProcessStageMappingService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, TokenCategoryRepository tokenCategoryRepository, TokenTypeRepository tokenTypeRepository, CounterRepository counterRepository, ProcessStageRepository processStageRepository, CounterProcessMappingRepository counterProcessMappingRepository) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
        this.tokenTypeRepository = tokenTypeRepository;
        this.counterRepository = counterRepository;
        this.processStageRepository = processStageRepository;
        this.counterProcessMappingRepository = counterProcessMappingRepository;
    }

    public List<ProcessStageResponsePayload> getProcessStageByCounterList(String orgCode, String deptCode, String catCode, String typeCode, String code) {
        List<ProcessStageResponsePayload> response = new ArrayList<>();
        Counter counter = validateCounter(orgCode,deptCode,catCode,typeCode,code);
        List<CounterProcessMapping> processTypeMap = counterProcessMappingRepository.findByCounterCode(counter.getId());
        if (!processTypeMap.isEmpty()) {
            processTypeMap.forEach(counterProcess -> {
                Optional<ProcessStage> stageOpt = processStageRepository.findById(counterProcess.getProcessStageCode());
                if (stageOpt.isPresent()) {
                    ProcessStageResponsePayload item = new ProcessStageResponsePayload();
                    item.setStatus(stageOpt.get().getStatus());
                    item.setOrganizationCode(stageOpt.get().getOrganizationCode());
                    item.setDepartmentCode(stageOpt.get().getDepartmentCode());
                    item.setTokenCategoryCode(stageOpt.get().getTokenCategoryCode());
                    item.setTokenTypeCode(stageOpt.get().getTokenTypeCode());
                    item.setCode(stageOpt.get().getCode());
                    item.setName(stageOpt.get().getName());
                    item.setStatus(stageOpt.get().getStatus());
                    response.add(item);
                }
            });
        }
        return response;
    }

    public List<CounterResponsePayload> getCounterByProcessStageList(String orgCode, String deptCode, String catCode, String typeCode, String stageCode) {
        List<CounterResponsePayload> response = new ArrayList<>();
        ProcessStage stage = validateStage(orgCode,deptCode,catCode,typeCode,stageCode);
        List<CounterProcessMapping> processTypeMap = counterProcessMappingRepository.findByProcessStageCode(stage.getId());
        if (!processTypeMap.isEmpty()) {
            processTypeMap.forEach(counterProcess -> {
                Optional<Counter> counterOpt = counterRepository.findById(counterProcess.getCounterCode());
                if (counterOpt.isPresent()) {
                    CounterResponsePayload item = new CounterResponsePayload();
                    item.setOrganizationCode(counterOpt.get().getOrganizationCode());
                    item.setDepartmentCode(counterOpt.get().getDepartmentCode());
                    item.setTokenCategoryCode(counterOpt.get().getTokenCategoryCode());
                    item.setTokenTypeCode(counterOpt.get().getTokenTypeCode());
                    item.setCounterNo(counterOpt.get().getCounterNo());
                    response.add(item);
                }
            });
        }
        return response;
    }

    public CounterProcessStageMapResponsePayload addProcessToCounterTree(CounterProcessStageMapCreateRequestPayload request) {
        Counter counter = null;
        CounterProcessStageMapResponsePayload response = new CounterProcessStageMapResponsePayload();
        List<CounterProcessMapping> counterProcessMappingList = validateAdd(request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                request.getTokenTypeCode(), request.getCounterCode(), request.getProcessStage());
        if (!counterProcessMappingList.isEmpty()) {
            throw new InvalidOperationException("An Active Mapping is already exists");
        }

        response.setOrganizationCode(request.getOrganizationCode());
        response.setDepartmentCode(request.getDepartmentCode());
        response.setTokenCategoryCode(request.getTokenCategoryCode());
        response.setTokenTypeCode(request.getTokenTypeCode());
        response.setCounterCode(request.getCounterCode());
        List<CounterProcessStageMapStatusResponsePayload> stageList = new ArrayList<>();
        for(String stageCode : request.getProcessStage()) {
            CounterProcessMapping counterProcessMapping = new CounterProcessMapping(
                    request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                    request.getTokenTypeCode(), request.getCounterCode(), stageCode, CREATED);
            counterProcessMapping = counterProcessMappingRepository.save(counterProcessMapping);
            CounterProcessStageMapStatusResponsePayload stage = new CounterProcessStageMapStatusResponsePayload(
                    counterProcessMapping.getId(), stageCode, counterProcessMapping.getStatus());
            stageList.add(stage);
        }
        response.setProcessStage(stageList);
        return response;
    }

    public CounterProcessStageMapResponsePayload updateStatusProcessToCounterTree(CounterProcessStageMapUpdateRequestPayload request) {
        Counter counter = null;
        ProcessStage processStage = null;
//        List<CounterProcessMapping> counterProcessMappingList = validateUpdate(request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
//                request.getTokenTypeCode(), request.getCounterCode(), request.getProcessStage());
//        if (!counterProcessMappingList.isEmpty()) {
//            throw new InvalidOperationException("An Active Mapping is already exists");
//        }
        List<CounterProcessMapping> existingMapping = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCode(
                request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                request.getTokenTypeCode(), request.getCounterCode());
        if (!existingMapping.isEmpty()) {
            existingMapping.forEach(map -> {
                map.setStatus(DELETED);
                counterProcessMappingRepository.save(map);
            });
        }
        List<CounterProcessStageMapStatusResponsePayload> stageList = new ArrayList<>();
        if (!request.getProcessStage().isEmpty()) {
            request.getProcessStage().forEach(stage -> {
                Optional<CounterProcessMapping> exists = existingMapping.stream().filter(pStage -> pStage.getProcessStageCode().equalsIgnoreCase(stage)).findFirst();
                if (exists.isPresent()) {
                    exists.get().setStatus(CREATED);
                    counterProcessMappingRepository.save(exists.get());
                    CounterProcessStageMapStatusResponsePayload stageRes = new CounterProcessStageMapStatusResponsePayload(
                            exists.get().getId(), stage, exists.get().getStatus());
                    stageList.add(stageRes);
                } else {
                    CounterProcessMapping newMap = new CounterProcessMapping();
                    newMap.setStatus(CREATED);
                    newMap.setCounterCode(request.getCounterCode());
                    newMap.setProcessStageCode(stage);
                    newMap.setOrganizationCode(request.getOrganizationCode());
                    newMap.setDepartmentCode(request.getDepartmentCode());
                    newMap.setTokenCategoryCode(request.getTokenCategoryCode());
                    newMap.setTokenTypeCode(request.getTokenTypeCode());
                    counterProcessMappingRepository.save(newMap);
                    CounterProcessStageMapStatusResponsePayload stageRes = new CounterProcessStageMapStatusResponsePayload(
                            newMap.getId(), stage, newMap.getStatus());
                    stageList.add(stageRes);
                }
            });
        }
        CounterProcessStageMapResponsePayload response = new CounterProcessStageMapResponsePayload();
        response.setOrganizationCode(request.getOrganizationCode());
        response.setDepartmentCode(request.getDepartmentCode());
        response.setTokenCategoryCode(request.getTokenCategoryCode());
        response.setTokenTypeCode(request.getTokenTypeCode());
        response.setCounterCode(request.getCounterCode());
        response.setProcessStage(stageList);
        return response;
    }

    private Counter validateCounter(String orgCode, String deptCode, String catCode, String typeCode, String code) {
        Counter counter;
        if (!StringUtils.isEmpty(orgCode)) {
            if (!StringUtils.isEmpty(deptCode)) {
                if (!StringUtils.isEmpty(catCode)) {
                    if (!StringUtils.isEmpty(typeCode)) {
                        counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(orgCode, deptCode, catCode, typeCode, code)
                                .orElseThrow(() -> new ResourceNotFoundException("Counter does not exists"));
                    } else {
                        counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(orgCode, deptCode, catCode, code)
                                .orElseThrow(() -> new ResourceNotFoundException("Counter does not exists"));
                    }
                } else {
                    counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndCode(orgCode, deptCode, code)
                            .orElseThrow(() -> new ResourceNotFoundException("Counter does not exists"));
                }
            } else {
                counter = counterRepository.findByOrganizationCodeAndCode(orgCode, code)
                        .orElseThrow(() -> new ResourceNotFoundException("Counter does not exists"));
            }
        } else {
            throw new InvalidDataException("Invalid request");
        }
        return counter;
    }

    private ProcessStage validateStage(String orgCode, String deptCode, String catCode, String typeCode, String code) {
        ProcessStage stage;
        if (!StringUtils.isEmpty(orgCode)) {
            if (!StringUtils.isEmpty(deptCode)) {
                if (!StringUtils.isEmpty(catCode)) {
                    if (!StringUtils.isEmpty(typeCode)) {
                        stage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(orgCode, deptCode, catCode, typeCode, code)
                                .orElseThrow(() -> new ResourceNotFoundException("Stage does not exists"));
                    } else {
                        stage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(orgCode, deptCode, catCode, code)
                                .orElseThrow(() -> new ResourceNotFoundException("Stage does not exists"));
                    }
                } else {
                    stage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndCode(orgCode, deptCode, code)
                            .orElseThrow(() -> new ResourceNotFoundException("Stage does not exists"));
                }
            } else {
                stage = processStageRepository.findByOrganizationCodeAndCode(orgCode, code)
                        .orElseThrow(() -> new ResourceNotFoundException("Stage does not exists"));
            }
        } else {
            throw new InvalidDataException("Invalid request");
        }
        return stage;
    }

    private List<CounterProcessMapping> validateAdd(String orgCode, String deptCode, String catCode, String typeCode, String counterCode, List<String> stageCodeList) {
        Counter counter = new Counter();
        List<ProcessStage> processStageList = new ArrayList<>();
        List<CounterProcessMapping> processMappingListFinal = new ArrayList<>();
        if (!StringUtils.isEmpty(orgCode)) {
            organizationRepository.findByCodeAndStatus(orgCode, CREATED)
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid Organization does not exists"));
            if (!StringUtils.isEmpty(deptCode)) {
                departmentRepository.findByOrganizationCodeAndCodeAndStatus(
                                orgCode, deptCode, CREATED)
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid Department does not exists"));
                if (!StringUtils.isEmpty(catCode)) {
                    tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCodeAndStatus(
                                    orgCode, deptCode, catCode, CREATED)
                            .orElseThrow(() -> new ResourceNotFoundException("Invalid TOken Category does not exists"));
                    if (!StringUtils.isEmpty(typeCode)) {
                        tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCodeAndStatus(
                                        orgCode, deptCode,
                                        catCode, typeCode, CREATED)
                                .orElseThrow(() -> new ResourceNotFoundException("Invalid TOken Category does not exists"));
                        if (!StringUtils.isEmpty(counterCode)) {
                            counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(
                                            orgCode, deptCode,
                                            catCode, typeCode, counterCode)
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        if (!stageCodeList.isEmpty()) {
                            for(String stageCode: stageCodeList) {
                                ProcessStage processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(
                                                orgCode, deptCode,
                                                catCode, typeCode, stageCode)
                                        .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                                List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCodeAndProcessStageCodeAndStatus(
                                        orgCode, deptCode,
                                        catCode, typeCode, counter.getId(), processStage.getId(), CREATED);
                                processMappingListFinal.addAll(processMappingList);
                            }
                        } else {
                            throw new InvalidOperationException("Process Stage is not provided for mapping");
                        }
                    } else {
                        if (!StringUtils.isEmpty(counterCode)) {
                            counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(
                                            orgCode, deptCode,
                                            catCode, counterCode)
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        if (!stageCodeList.isEmpty()) {
                            for(String stageCode: stageCodeList) {
                                ProcessStage processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(
                                                orgCode, deptCode,
                                                catCode, stageCode)
                                        .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                                List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterCodeAndProcessStageCodeAndStatus(
                                        orgCode, deptCode,
                                        catCode, counter.getId(), processStage.getId(), CREATED);
                                processMappingListFinal.addAll(processMappingList);
                            }
                        } else {
                            throw new InvalidOperationException("Process Stage is not provided for mapping");
                        }
                    }
                } else {
                    if (!StringUtils.isEmpty(counterCode)) {
                        counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndCode(
                                        orgCode, deptCode,
                                        counterCode)
                                .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                    }
                    if (!stageCodeList.isEmpty()) {
                        for(String stageCode: stageCodeList) {
                            ProcessStage processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndCode(
                                            orgCode, deptCode, stageCode)
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                            List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndCounterCodeAndProcessStageCodeAndStatus(
                                    orgCode, deptCode,
                                    counter.getId(), processStage.getId(), CREATED);
                            processMappingListFinal.addAll(processMappingList);
                        }
                    } else {
                        throw new InvalidOperationException("Process Stage is not provided for mapping");
                    }
                }
            } else {
                if (!StringUtils.isEmpty(counterCode)) {
                    counter = counterRepository.findByOrganizationCodeAndCode(
                                    orgCode, counterCode)
                            .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                }
                if (!stageCodeList.isEmpty()) {
                    for(String stageCode: stageCodeList) {
                        ProcessStage processStage = processStageRepository.findByOrganizationCodeAndCode(
                                        orgCode, stageCode)
                                .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndCounterCodeAndProcessStageCodeAndStatus(
                                orgCode,
                                counter.getId(), processStage.getId(), CREATED);
                        processMappingListFinal.addAll(processMappingList);
                    }
                } else {
                    throw new InvalidOperationException("Process Stage is not provided for mapping");
                }
            }
        }

        return processMappingListFinal;
    }

    private List<CounterProcessMapping> validateUpdate(String orgCode, String deptCode, String catCode, String typeCode, String counterCode, List<String> stageCodeList) {
        Counter counter = new Counter();
        List<ProcessStage> processStageList = new ArrayList<>();
        List<CounterProcessMapping> processMappingListFinal = new ArrayList<>();
        if (!StringUtils.isEmpty(orgCode)) {
            organizationRepository.findByCodeAndStatus(orgCode, CREATED)
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid Organization does not exists"));
            if (!StringUtils.isEmpty(deptCode)) {
                departmentRepository.findByOrganizationCodeAndCodeAndStatus(
                                orgCode, deptCode, CREATED)
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid Department does not exists"));
                if (!StringUtils.isEmpty(catCode)) {
                    tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCodeAndStatus(
                                    orgCode, deptCode, catCode, CREATED)
                            .orElseThrow(() -> new ResourceNotFoundException("Invalid TOken Category does not exists"));
                    if (!StringUtils.isEmpty(typeCode)) {
                        tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCodeAndStatus(
                                        orgCode, deptCode,
                                        catCode, typeCode, CREATED)
                                .orElseThrow(() -> new ResourceNotFoundException("Invalid TOken Category does not exists"));
                        if (!StringUtils.isEmpty(counterCode)) {
                            counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(
                                            orgCode, deptCode,
                                            catCode, typeCode, counterCode)
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        if (!stageCodeList.isEmpty()) {
                            for(String stageRequest: stageCodeList) {
                                ProcessStage processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(
                                                orgCode, deptCode,
                                                catCode, typeCode, stageRequest)
                                        .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                                List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCodeAndProcessStageCodeAndStatus(
                                        orgCode, deptCode,
                                        catCode, typeCode, counter.getId(), processStage.getId(), CREATED);
                                processMappingListFinal.addAll(processMappingList);
                            }
                        } else {
                            throw new InvalidOperationException("Process Stage is not provided for mapping");
                        }
                    } else {
                        if (!StringUtils.isEmpty(counterCode)) {
                            counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(
                                            orgCode, deptCode,
                                            catCode, counterCode)
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        if (!stageCodeList.isEmpty()) {
                            for(String stageRequest: stageCodeList) {
                                ProcessStage processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(
                                                orgCode, deptCode,
                                                catCode, stageRequest)
                                        .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                                List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterCodeAndProcessStageCodeAndStatus(
                                        orgCode, deptCode,
                                        catCode, counter.getId(), processStage.getId(), CREATED);
                                processMappingListFinal.addAll(processMappingList);
                            }
                        } else {
                            throw new InvalidOperationException("Process Stage is not provided for mapping");
                        }
                    }
                } else {
                    if (!StringUtils.isEmpty(counterCode)) {
                        counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndCode(
                                        orgCode, deptCode,
                                        counterCode)
                                .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                    }
                    if (!stageCodeList.isEmpty()) {
                        for(String stageRequest: stageCodeList) {
                            ProcessStage processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndCode(
                                            orgCode, deptCode, stageRequest)
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                            List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndCounterCodeAndProcessStageCodeAndStatus(
                                    orgCode, deptCode,
                                    counter.getId(), processStage.getId(), CREATED);
                            processMappingListFinal.addAll(processMappingList);
                        }
                    } else {
                        throw new InvalidOperationException("Process Stage is not provided for mapping");
                    }
                }
            } else {
                if (!StringUtils.isEmpty(counterCode)) {
                    counter = counterRepository.findByOrganizationCodeAndCode(
                                    orgCode, counterCode)
                            .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                }
                if (!stageCodeList.isEmpty()) {
                    for(String stageRequest: stageCodeList) {
                        ProcessStage processStage = processStageRepository.findByOrganizationCodeAndCode(
                                        orgCode, stageRequest)
                                .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndCounterCodeAndProcessStageCodeAndStatus(
                                orgCode,
                                counter.getId(), processStage.getId(), CREATED);
                        processMappingListFinal.addAll(processMappingList);
                    }
                } else {
                    throw new InvalidOperationException("Process Stage is not provided for mapping");
                }
            }
        }

        return processMappingListFinal;
    }
}
