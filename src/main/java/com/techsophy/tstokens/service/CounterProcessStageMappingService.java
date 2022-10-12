package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.rule.*;
import com.techsophy.tstokens.entity.*;
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
        Counter counter = null;
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
        List<CounterProcessMapping> processTypeMap = counterProcessMappingRepository.findByCounterId(counter.getId());
        if (!processTypeMap.isEmpty()) {
            processTypeMap.forEach(counterProcess -> {
                Optional<ProcessStage> stageOpt = processStageRepository.findById(counterProcess.getProcessStageId());
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
        ProcessStage stage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(orgCode, deptCode, catCode, typeCode, stageCode)
                .orElseThrow(() -> new ResourceNotFoundException("Counter does not exists"));
        List<CounterProcessMapping> processTypeMap = counterProcessMappingRepository.findByProcessStageId(stage.getId());
        if (!processTypeMap.isEmpty()) {
            processTypeMap.forEach(counterProcess -> {
                Optional<Counter> counterOpt = counterRepository.findById(counterProcess.getCounterId());
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
        ProcessStage processStage = null;
        if (!StringUtils.isEmpty(request.getOrganizationCode())) {
            organizationRepository.findByCodeAndStatus(request.getOrganizationCode(), CREATED)
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid Organization does not exists"));
            if (!StringUtils.isEmpty(request.getDepartmentCode())) {
                departmentRepository.findByOrganizationCodeAndCodeAndStatus(
                        request.getOrganizationCode(), request.getDepartmentCode(), CREATED)
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid Department does not exists"));
                if (!StringUtils.isEmpty(request.getTokenCategoryCode())) {
                    tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCodeAndStatus(
                                    request.getOrganizationCode(), request.getDepartmentCode(),
                                    request.getTokenCategoryCode(), CREATED)
                            .orElseThrow(() -> new ResourceNotFoundException("Invalid TOken Category does not exists"));
                    if (!StringUtils.isEmpty(request.getTokenTypeCode())) {
                        tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCodeAndStatus(
                                        request.getOrganizationCode(), request.getDepartmentCode(),
                                        request.getTokenCategoryCode(), request.getTokenTypeCode(), CREATED)
                                .orElseThrow(() -> new ResourceNotFoundException("Invalid TOken Category does not exists"));
                        if (!StringUtils.isEmpty(request.getCounterCode())) {
                            counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(
                                            request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                            request.getTokenTypeCode(), request.getCounterCode())
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        if (!StringUtils.isEmpty(request.getProcessStageCode())) {
                            processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(
                                    request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                    request.getTokenTypeCode(), request.getProcessStageCode())
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterIdAndProcessStageIdAndStatus(
                                request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                request.getTokenTypeCode(), counter.getId(), processStage.getId(), CREATED);
                        if(!processMappingList.isEmpty()) {
                            throw new InvalidOperationException("Active Mapping already exists");
                        }
                    } else {
                        if (!StringUtils.isEmpty(request.getCounterCode())) {
                            counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(
                                            request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                            request.getCounterCode())
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        if (!StringUtils.isEmpty(request.getProcessStageCode())) {
                            processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(
                                            request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                            request.getProcessStageCode())
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterIdAndProcessStageIdAndStatus(
                                request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                counter.getId(), processStage.getId(), CREATED);
                        if(!processMappingList.isEmpty()) {
                            throw new InvalidOperationException("Active Mapping already exists");
                        }
                    }
                } else {
                    if (!StringUtils.isEmpty(request.getCounterCode())) {
                        counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndCode(
                                        request.getOrganizationCode(), request.getDepartmentCode(),
                                        request.getCounterCode())
                                .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                    }
                    if (!StringUtils.isEmpty(request.getProcessStageCode())) {
                        processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndCode(
                                        request.getOrganizationCode(), request.getDepartmentCode(),
                                        request.getProcessStageCode())
                                .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                    }
                    List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndCounterIdAndProcessStageIdAndStatus(
                            request.getOrganizationCode(), request.getDepartmentCode(),
                            counter.getId(), processStage.getId(), CREATED);
                    if(!processMappingList.isEmpty()) {
                        throw new InvalidOperationException("Active Mapping already exists");
                    }
                }
            } else {
                if (!StringUtils.isEmpty(request.getCounterCode())) {
                    counter = counterRepository.findByOrganizationCodeAndCode(
                                    request.getOrganizationCode(),
                                    request.getCounterCode())
                            .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                }
                if (!StringUtils.isEmpty(request.getProcessStageCode())) {
                    processStage = processStageRepository.findByOrganizationCodeAndCode(
                                    request.getOrganizationCode(),
                                    request.getProcessStageCode())
                            .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                }
                List<CounterProcessMapping> processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndCounterIdAndProcessStageIdAndStatus(
                        request.getOrganizationCode(),
                        counter.getId(), processStage.getId(), CREATED);
                if(!processMappingList.isEmpty()) {
                    throw new InvalidOperationException("Active Mapping already exists");
                }
            }
        } else {
            throw new InvalidDataException("Invalid request");
        }
        CounterProcessMapping counterProcessMapping = new CounterProcessMapping(
                request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                request.getTokenTypeCode(), counter.getId(), processStage.getId(), CREATED);
        counterProcessMapping = counterProcessMappingRepository.save(counterProcessMapping);

        return new CounterProcessStageMapResponsePayload(
                counterProcessMapping.getId(),
                counterProcessMapping.getOrganizationCode(),
                counterProcessMapping.getDepartmentCode(),
                counterProcessMapping.getTokenCategoryCode(),
                counterProcessMapping.getTokenTypeCode(),
                counterProcessMapping.getCounterId(),
                counterProcessMapping.getProcessStageId(),
                counterProcessMapping.getStatus());
    }

    public CounterProcessStageMapResponsePayload updateStatusProcessToCounterTree(CounterProcessStageMapUpdateRequestPayload request) {
        Counter counter = null;
        ProcessStage processStage = null;
        List<CounterProcessMapping> processMappingList = new ArrayList<>();
        if (!StringUtils.isEmpty(request.getOrganizationCode())) {
            organizationRepository.findByCodeAndStatus(request.getOrganizationCode(), CREATED)
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid Organization does not exists"));
            if (!StringUtils.isEmpty(request.getDepartmentCode())) {
                departmentRepository.findByOrganizationCodeAndCodeAndStatus(
                                request.getOrganizationCode(), request.getDepartmentCode(), CREATED)
                        .orElseThrow(() -> new ResourceNotFoundException("Invalid Department does not exists"));
                if (!StringUtils.isEmpty(request.getTokenCategoryCode())) {
                    tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCodeAndStatus(
                                    request.getOrganizationCode(), request.getDepartmentCode(),
                                    request.getTokenCategoryCode(), CREATED)
                            .orElseThrow(() -> new ResourceNotFoundException("Invalid TOken Category does not exists"));
                    if (!StringUtils.isEmpty(request.getTokenTypeCode())) {
                        tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCodeAndStatus(
                                        request.getOrganizationCode(), request.getDepartmentCode(),
                                        request.getTokenCategoryCode(), request.getTokenTypeCode(), CREATED)
                                .orElseThrow(() -> new ResourceNotFoundException("Invalid TOken Category does not exists"));
                        if (!StringUtils.isEmpty(request.getCounterCode())) {
                            counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(
                                            request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                            request.getTokenTypeCode(), request.getCounterCode())
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        if (!StringUtils.isEmpty(request.getProcessStageCode())) {
                            processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(
                                            request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                            request.getTokenTypeCode(), request.getProcessStageCode())
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterIdAndProcessStageIdAndStatus(
                                request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                request.getTokenTypeCode(), counter.getId(), processStage.getId(), CREATED);
                        if(processMappingList.isEmpty()) {
                            throw new InvalidOperationException("Active Mapping already exists");
                        }
                    } else {
                        if (!StringUtils.isEmpty(request.getCounterCode())) {
                            counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(
                                            request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                            request.getCounterCode())
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        if (!StringUtils.isEmpty(request.getProcessStageCode())) {
                            processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(
                                            request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                            request.getProcessStageCode())
                                    .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                        }
                        processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterIdAndProcessStageIdAndStatus(
                                request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                                counter.getId(), processStage.getId(), CREATED);
                        if(processMappingList.isEmpty()) {
                            throw new InvalidOperationException("Active Mapping already exists");
                        }
                    }
                } else {
                    if (!StringUtils.isEmpty(request.getCounterCode())) {
                        counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndCode(
                                        request.getOrganizationCode(), request.getDepartmentCode(),
                                        request.getCounterCode())
                                .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                    }
                    if (!StringUtils.isEmpty(request.getProcessStageCode())) {
                        processStage = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndCode(
                                        request.getOrganizationCode(), request.getDepartmentCode(),
                                        request.getProcessStageCode())
                                .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                    }
                    processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndDepartmentCodeAndCounterIdAndProcessStageIdAndStatus(
                            request.getOrganizationCode(), request.getDepartmentCode(),
                            counter.getId(), processStage.getId(), CREATED);
                    if(processMappingList.isEmpty()) {
                        throw new InvalidOperationException("Active Mapping already exists");
                    }
                }
            } else {
                if (!StringUtils.isEmpty(request.getCounterCode())) {
                    counter = counterRepository.findByOrganizationCodeAndCode(
                                    request.getOrganizationCode(),
                                    request.getCounterCode())
                            .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                }
                if (!StringUtils.isEmpty(request.getProcessStageCode())) {
                    processStage = processStageRepository.findByOrganizationCodeAndCode(
                                    request.getOrganizationCode(),
                                    request.getProcessStageCode())
                            .orElseThrow(() -> new ResourceNotFoundException("Process Stage does not exists"));
                }
                processMappingList = counterProcessMappingRepository.findByOrganizationCodeAndCounterIdAndProcessStageIdAndStatus(
                        request.getOrganizationCode(),
                        counter.getId(), processStage.getId(), CREATED);
                if(processMappingList.isEmpty()) {
                    throw new InvalidOperationException("Active Mapping already exists");
                }
            }
        } else {
            throw new InvalidDataException("Invalid request");
        }
        CounterProcessMapping counterProcessMapping = processMappingList.get(0);
        counterProcessMapping.setStatus(request.getStatus());
        counterProcessMapping = counterProcessMappingRepository.save(counterProcessMapping);

        return new CounterProcessStageMapResponsePayload(
                counterProcessMapping.getId(),
                counterProcessMapping.getOrganizationCode(),
                counterProcessMapping.getDepartmentCode(),
                counterProcessMapping.getTokenCategoryCode(),
                counterProcessMapping.getTokenTypeCode(),
                counterProcessMapping.getCounterId(),
                counterProcessMapping.getProcessStageId(),
                counterProcessMapping.getStatus());
    }
}
