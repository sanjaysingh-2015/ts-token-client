package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.rule.*;
import com.techsophy.tstokens.entity.Counter;
import com.techsophy.tstokens.entity.CounterDeviceMapping;
import com.techsophy.tstokens.entity.Device;
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
public class CounterDeviceMappingService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final CounterRepository counterRepository;
    private final DeviceRepository deviceRepository;
    private final CounterDeviceMappingRepository counterDeviceMappingRepository;

    private static final String CREATED = "ACTIVE";
    private static final String DELETED = "DELETED";

    @Autowired
    public CounterDeviceMappingService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, TokenCategoryRepository tokenCategoryRepository, TokenTypeRepository tokenTypeRepository, CounterRepository counterRepository, DeviceRepository deviceRepository, CounterDeviceMappingRepository counterDeviceMappingRepository) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
        this.tokenTypeRepository = tokenTypeRepository;
        this.counterRepository = counterRepository;
        this.deviceRepository = deviceRepository;
        this.counterDeviceMappingRepository = counterDeviceMappingRepository;
    }

    public List<DeviceResponsePayload> getDeviceByCounterList(String orgCode, String deptCode, String catCode, String typeCode, String code) {
        List<DeviceResponsePayload> response = new ArrayList<>();
        Counter counter = validateCounter(orgCode,deptCode,catCode,typeCode,code);
        List<CounterDeviceMapping> processTypeMap = counterDeviceMappingRepository.findByCounterCode(counter.getId());
        if (!processTypeMap.isEmpty()) {
            processTypeMap.forEach(counterProcess -> {
                Optional<Device> deviceOpt = deviceRepository.findById(counterProcess.getDeviceUid());
                if (deviceOpt.isPresent()) {
                    DeviceResponsePayload item = new DeviceResponsePayload();
                    item.setStatus(deviceOpt.get().getStatus());
                    item.setOrganizationCode(deviceOpt.get().getOrganizationCode());
                    item.setDepartmentCode(deviceOpt.get().getDepartmentCode());
                    item.setTokenCategoryCode(deviceOpt.get().getTokenCategoryCode());
                    item.setTokenTypeCode(deviceOpt.get().getTokenTypeCode());
                    item.setDeviceUid(deviceOpt.get().getDeviceUid());
                    item.setDeviceName(deviceOpt.get().getDeviceName());
                    item.setDeviceLayout(deviceOpt.get().getDeviceLayout());
                    item.setDeviceType(deviceOpt.get().getDeviceType());
                    item.setIpAddress(deviceOpt.get().getIpAddress());
                    item.setPort(deviceOpt.get().getPort());
                    item.setStatus(deviceOpt.get().getStatus());
                    response.add(item);
                }
            });
        }
        return response;
    }

    public List<CounterResponsePayload> getCounterByDeviceList(String orgCode, String deptCode, String catCode, String typeCode, String stageCode) {
        List<CounterResponsePayload> response = new ArrayList<>();
        Device stage = validateStage(orgCode,deptCode,catCode,typeCode,stageCode);
        List<CounterDeviceMapping> processTypeMap = counterDeviceMappingRepository.findByDeviceUid(stage.getId());
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

    public CounterDeviceMapResponsePayload addDeviceToCounterTree(CounterDeviceMapCreateRequestPayload request) {
        Counter counter = null;
        CounterDeviceMapResponsePayload response = new CounterDeviceMapResponsePayload();
////        List<CounterDeviceMapping> counterProcessMappingList = validateAdd(request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
////                request.getTokenTypeCode(), request.getCounterCode(), request.getDevices());
//        if (!counterProcessMappingList.isEmpty()) {
//            throw new InvalidOperationException("An Active Mapping is already exists");
//        }

        response.setOrganizationCode(request.getOrganizationCode());
        response.setDepartmentCode(request.getDepartmentCode());
        response.setTokenCategoryCode(request.getTokenCategoryCode());
        response.setTokenTypeCode(request.getTokenTypeCode());
        response.setCounterCode(request.getCounterCode());
        List<CounterDeviceMapStatusResponsePayload> deviceList = new ArrayList<>();
        for(String deviceUid : request.getDevices()) {
            CounterDeviceMapping counterDeviceMapping = new CounterDeviceMapping(
                    request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                    request.getTokenTypeCode(), request.getCounterCode(), deviceUid, CREATED);
            counterDeviceMapping = counterDeviceMappingRepository.save(counterDeviceMapping);
            CounterDeviceMapStatusResponsePayload stage = new CounterDeviceMapStatusResponsePayload(
                    counterDeviceMapping.getId(), deviceUid, counterDeviceMapping.getStatus());
            deviceList.add(stage);
        }
        response.setDevices(deviceList);
        return response;
    }

    public CounterDeviceMapResponsePayload updateStatusProcessToCounterTree(CounterDeviceMapUpdateRequestPayload request) {
        Counter counter = null;
        Device device = null;
        List<CounterDeviceMapping> existingMapping = counterDeviceMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCode(
                request.getOrganizationCode(), request.getDepartmentCode(), request.getTokenCategoryCode(),
                request.getTokenTypeCode(), request.getCounterCode());
        if (!existingMapping.isEmpty()) {
            existingMapping.forEach(map -> {
                map.setStatus(DELETED);
                counterDeviceMappingRepository.save(map);
            });
        }
        List<CounterDeviceMapStatusResponsePayload> deviceList = new ArrayList<>();
        if (!request.getDevices().isEmpty()) {
            request.getDevices().forEach(deviceUid -> {
                Optional<CounterDeviceMapping> exists = existingMapping.stream().filter(pStage -> pStage.getDeviceUid().equalsIgnoreCase(deviceUid)).findFirst();
                if (exists.isPresent()) {
                    exists.get().setStatus(CREATED);
                    counterDeviceMappingRepository.save(exists.get());
                    CounterDeviceMapStatusResponsePayload stageRes = new CounterDeviceMapStatusResponsePayload(
                            exists.get().getId(), deviceUid, exists.get().getStatus());
                    deviceList.add(stageRes);
                } else {
                    CounterDeviceMapping newMap = new CounterDeviceMapping();
                    newMap.setStatus(CREATED);
                    newMap.setCounterCode(request.getCounterCode());
                    newMap.setDeviceUid(deviceUid);
                    newMap.setOrganizationCode(request.getOrganizationCode());
                    newMap.setDepartmentCode(request.getDepartmentCode());
                    newMap.setTokenCategoryCode(request.getTokenCategoryCode());
                    newMap.setTokenTypeCode(request.getTokenTypeCode());
                    counterDeviceMappingRepository.save(newMap);
                    CounterDeviceMapStatusResponsePayload stageRes = new CounterDeviceMapStatusResponsePayload(
                            newMap.getId(), deviceUid, newMap.getStatus());
                    deviceList.add(stageRes);
                }
            });
        }
        CounterDeviceMapResponsePayload response = new CounterDeviceMapResponsePayload();
        response.setOrganizationCode(request.getOrganizationCode());
        response.setDepartmentCode(request.getDepartmentCode());
        response.setTokenCategoryCode(request.getTokenCategoryCode());
        response.setTokenTypeCode(request.getTokenTypeCode());
        response.setCounterCode(request.getCounterCode());
        response.setDevices(deviceList);
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

    private Device validateStage(String orgCode, String deptCode, String catCode, String typeCode, String code) {
        Device device;
        if (!StringUtils.isEmpty(orgCode)) {
            if (!StringUtils.isEmpty(deptCode)) {
                if (!StringUtils.isEmpty(catCode)) {
                    if (!StringUtils.isEmpty(typeCode)) {
                        device = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndDeviceUid(orgCode, deptCode, catCode, typeCode, code)
                                .orElseThrow(() -> new ResourceNotFoundException("Stage does not exists"));
                    } else {
                        device = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndDeviceUid(orgCode, deptCode, catCode, code)
                                .orElseThrow(() -> new ResourceNotFoundException("Stage does not exists"));
                    }
                } else {
                    device = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndDeviceUid(orgCode, deptCode, code)
                            .orElseThrow(() -> new ResourceNotFoundException("Stage does not exists"));
                }
            } else {
                device = deviceRepository.findByOrganizationCodeAndDeviceUid(orgCode, code)
                        .orElseThrow(() -> new ResourceNotFoundException("Stage does not exists"));
            }
        } else {
            throw new InvalidDataException("Invalid request");
        }
        return device;
    }

    private List<CounterDeviceMapping> validateAdd(String orgCode, String deptCode, String catCode, String typeCode, String counterCode, List<String> deviceUidList) {
        Counter counter = new Counter();
        List<Device> deviceList = new ArrayList<>();
        List<CounterDeviceMapping> processMappingListFinal = new ArrayList<>();
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
                                    .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                        }
                        if (!deviceUidList.isEmpty()) {
                            for(String deviceUid: deviceUidList) {
                                Device device = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndDeviceUid(
                                                orgCode, deptCode,
                                                catCode, typeCode, deviceUid)
                                        .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                                List<CounterDeviceMapping> processMappingList = counterDeviceMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCodeAndDeviceUidAndStatus(
                                        orgCode, deptCode,
                                        catCode, typeCode, counter.getId(), device.getId(), CREATED);
                                processMappingListFinal.addAll(processMappingList);
                            }
                        } else {
                            throw new InvalidOperationException("Device is not provided for mapping");
                        }
                    } else {
                        if (!StringUtils.isEmpty(counterCode)) {
                            counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(
                                            orgCode, deptCode,
                                            catCode, counterCode)
                                    .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                        }
                        if (!deviceUidList.isEmpty()) {
                            for(String deviceUid: deviceUidList) {
                                Device device = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndDeviceUid(
                                                orgCode, deptCode,
                                                catCode, deviceUid)
                                        .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                                List<CounterDeviceMapping> processMappingList = counterDeviceMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterCodeAndDeviceUidAndStatus(
                                        orgCode, deptCode,
                                        catCode, counter.getId(), device.getId(), CREATED);
                                processMappingListFinal.addAll(processMappingList);
                            }
                        } else {
                            throw new InvalidOperationException("Device is not provided for mapping");
                        }
                    }
                } else {
                    if (!StringUtils.isEmpty(counterCode)) {
                        counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndCode(
                                        orgCode, deptCode,
                                        counterCode)
                                .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                    }
                    if (!deviceUidList.isEmpty()) {
                        for(String stageCode: deviceUidList) {
                            Device device = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndDeviceUid(
                                            orgCode, deptCode, stageCode)
                                    .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                            List<CounterDeviceMapping> processMappingList = counterDeviceMappingRepository.findByOrganizationCodeAndDepartmentCodeAndCounterCodeAndDeviceUidAndStatus(
                                    orgCode, deptCode,
                                    counter.getId(), device.getId(), CREATED);
                            processMappingListFinal.addAll(processMappingList);
                        }
                    } else {
                        throw new InvalidOperationException("Device is not provided for mapping");
                    }
                }
            } else {
                if (!StringUtils.isEmpty(counterCode)) {
                    counter = counterRepository.findByOrganizationCodeAndCode(
                                    orgCode, counterCode)
                            .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                }
                if (!deviceUidList.isEmpty()) {
                    for(String stageCode: deviceUidList) {
                        Device device = deviceRepository.findByOrganizationCodeAndDeviceUid(
                                        orgCode, stageCode)
                                .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                        List<CounterDeviceMapping> processMappingList = counterDeviceMappingRepository.findByOrganizationCodeAndCounterCodeAndDeviceUidAndStatus(
                                orgCode,
                                counter.getId(), device.getId(), CREATED);
                        processMappingListFinal.addAll(processMappingList);
                    }
                } else {
                    throw new InvalidOperationException("Device is not provided for mapping");
                }
            }
        }

        return processMappingListFinal;
    }

    private List<CounterDeviceMapping> validateUpdate(String orgCode, String deptCode, String catCode, String typeCode, String counterCode, List<String> deviceUidList) {
        Counter counter = new Counter();
        List<Device> deviceList = new ArrayList<>();
        List<CounterDeviceMapping> processMappingListFinal = new ArrayList<>();
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
                                    .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                        }
                        if (!deviceUidList.isEmpty()) {
                            for(String stageRequest: deviceUidList) {
                                Device device = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndDeviceUid(
                                                orgCode, deptCode,
                                                catCode, typeCode, stageRequest)
                                        .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                                List<CounterDeviceMapping> processMappingList = counterDeviceMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCodeAndDeviceUidAndStatus(
                                        orgCode, deptCode,
                                        catCode, typeCode, counter.getId(), device.getId(), CREATED);
                                processMappingListFinal.addAll(processMappingList);
                            }
                        } else {
                            throw new InvalidOperationException("Device is not provided for mapping");
                        }
                    } else {
                        if (!StringUtils.isEmpty(counterCode)) {
                            counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(
                                            orgCode, deptCode,
                                            catCode, counterCode)
                                    .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                        }
                        if (!deviceUidList.isEmpty()) {
                            for(String deviceUid: deviceUidList) {
                                Device device = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndDeviceUid(
                                                orgCode, deptCode,
                                                catCode, deviceUid)
                                        .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                                List<CounterDeviceMapping> processMappingList = counterDeviceMappingRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterCodeAndDeviceUidAndStatus(
                                        orgCode, deptCode,
                                        catCode, counter.getId(), device.getId(), CREATED);
                                processMappingListFinal.addAll(processMappingList);
                            }
                        } else {
                            throw new InvalidOperationException("Device is not provided for mapping");
                        }
                    }
                } else {
                    if (!StringUtils.isEmpty(counterCode)) {
                        counter = counterRepository.findByOrganizationCodeAndDepartmentCodeAndCode(
                                        orgCode, deptCode,
                                        counterCode)
                                .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                    }
                    if (!deviceUidList.isEmpty()) {
                        for(String deviceUid: deviceUidList) {
                            Device device = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndDeviceUid(
                                            orgCode, deptCode, deviceUid)
                                    .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                            List<CounterDeviceMapping> processMappingList = counterDeviceMappingRepository.findByOrganizationCodeAndDepartmentCodeAndCounterCodeAndDeviceUidAndStatus(
                                    orgCode, deptCode,
                                    counter.getId(), device.getId(), CREATED);
                            processMappingListFinal.addAll(processMappingList);
                        }
                    } else {
                        throw new InvalidOperationException("Device is not provided for mapping");
                    }
                }
            } else {
                if (!StringUtils.isEmpty(counterCode)) {
                    counter = counterRepository.findByOrganizationCodeAndCode(
                                    orgCode, counterCode)
                            .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                }
                if (!deviceUidList.isEmpty()) {
                    for(String deviceUid: deviceUidList) {
                        Device device = deviceRepository.findByOrganizationCodeAndDeviceUid(
                                        orgCode, deviceUid)
                                .orElseThrow(() -> new ResourceNotFoundException("Device does not exists"));
                        List<CounterDeviceMapping> processMappingList = counterDeviceMappingRepository.findByOrganizationCodeAndCounterCodeAndDeviceUidAndStatus(
                                orgCode,
                                counter.getId(), device.getId(), CREATED);
                        processMappingListFinal.addAll(processMappingList);
                    }
                } else {
                    throw new InvalidOperationException("Device is not provided for mapping");
                }
            }
        }

        return processMappingListFinal;
    }
}
