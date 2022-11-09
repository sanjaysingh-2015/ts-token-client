package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.rule.DeviceCreateRequestPayload;
import com.techsophy.tstokens.dto.rule.DeviceResponsePayload;
import com.techsophy.tstokens.dto.rule.DeviceUpdateRequestPayload;
import com.techsophy.tstokens.entity.Device;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.*;
import com.techsophy.tstokens.utils.ApplicationMapping;
import com.techsophy.tstokens.utils.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DeviceService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final ValidationUtils validationUtils;
    private final TokenCategoryService tokenCategoryService;
    private final TokenTypeRepository tokenTypeRepository;
    private final DeviceRepository deviceRepository;
    private static final String CREATED = "ACTIVE";
    private static final String DELETED = "DELETED";

    @Autowired
    public DeviceService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, ValidationUtils validationUtils, TokenCategoryService tokenCategoryService, TokenTypeRepository tokenTypeRepository, DeviceRepository deviceRepository, TokenCategoryRepository tokenCategoryRepository) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.validationUtils = validationUtils;
        this.tokenCategoryService = tokenCategoryService;
        this.tokenTypeRepository = tokenTypeRepository;
        this.deviceRepository = deviceRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
    }

    public DeviceResponsePayload getDeviceDetails(String orgCode, String deptCode, String catCode, String tokenTypeCode, String deviceUid) {
        logger.info("In getDeviceDetails()");
        //TODO: Validation
        Optional<Device> deviceOpt = Optional.empty();
        if (!StringUtils.isEmpty(orgCode)) {
            if (!StringUtils.isEmpty(deptCode)) {
                if (!StringUtils.isEmpty(catCode)) {
                    if (!StringUtils.isEmpty(tokenTypeCode)) {
                        deviceOpt = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndDeviceUid(orgCode, deptCode, catCode, tokenTypeCode, deviceUid);
                    } else {
                        deviceOpt = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndDeviceUid(orgCode, deptCode, catCode, deviceUid);
                    }
                } else {
                    deviceOpt = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndDeviceUid(orgCode, deptCode, deviceUid);
                }
            } else {
                deviceOpt = deviceRepository.findByOrganizationCodeAndDeviceUid(orgCode, deviceUid);
            }
        } else {
            deviceOpt = deviceRepository.findByDeviceUid(deviceUid);
        }
        DeviceResponsePayload response = null;
        ApplicationMapping<DeviceResponsePayload, Device> responseMapping = new ApplicationMapping<>();
        if (deviceOpt.isPresent()) {
            response = responseMapping.convert(deviceOpt.get(), DeviceResponsePayload.class);
        } else {
            throw new ResourceNotFoundException("TS908- Invalid input, supplied data does not exists");
        }
        return response;
    }

    public List<DeviceResponsePayload> getDeviceList(String orgCode, String deptCode, String catCode, String tokenTypeCode) {
        logger.info("In getDeviceDetails()");
        List<Device> deviceList;
        if (!StringUtils.isEmpty(orgCode)) {
            if (!StringUtils.isEmpty(deptCode)) {
                if (!StringUtils.isEmpty(catCode)) {
                    if (!StringUtils.isEmpty(tokenTypeCode)) {
                        deviceList = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(orgCode, deptCode, catCode, tokenTypeCode);
                    } else {
                        deviceList = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(orgCode, deptCode, catCode);
                    }
                } else {
                    deviceList = deviceRepository.findByOrganizationCodeAndDepartmentCode(orgCode, deptCode);
                }
            } else {
                deviceList = deviceRepository.findByOrganizationCode(orgCode);
            }
        } else {
            deviceList = deviceRepository.findAll();
        }
        List<DeviceResponsePayload> response = new ArrayList<>();
        ApplicationMapping<DeviceResponsePayload, Device> responseMapping = new ApplicationMapping<>();
        if (!deviceList.isEmpty()) {
            deviceList.forEach(device ->
                    response.add(responseMapping.convert(device, DeviceResponsePayload.class))
            );
        }
        return response;
    }

    public DeviceResponsePayload createDevice(DeviceCreateRequestPayload requestPayload) {
        logger.info("In createDevice()");

        Map<String, String> errors = new HashMap<>();
        //validationUtils.validateDepartment(requestPayload, errors);
        ApplicationMapping<Device, DeviceCreateRequestPayload> mapping = new ApplicationMapping<>();
        Device device = mapping.convert(requestPayload, Device.class);
        return saveDevice(device);
    }

    public DeviceResponsePayload saveDevice(Device device) {
        logger.info("In saveDevice()");
        organizationRepository.findByCodeAndStatus(device.getOrganizationCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Organization Code Provided"));
        if(!StringUtils.isEmpty(device.getDepartmentCode())) {
            departmentRepository.findByOrganizationCodeAndCodeAndStatus(device.getOrganizationCode(), device.getDepartmentCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Department Code Provided"));
        }
        if(!StringUtils.isEmpty(device.getTokenCategoryCode())) {
            tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCodeAndStatus(device.getOrganizationCode(), device.getDepartmentCode(), device.getTokenCategoryCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Category Code Provided"));
        }
        if(!StringUtils.isEmpty(device.getTokenTypeCode())) {
            tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCodeAndStatus(device.getOrganizationCode(), device.getDepartmentCode(), device.getTokenCategoryCode(), device.getTokenTypeCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Token Type Code Provided"));
        }
        device.setCreatedOn(new Date());
        device.setStatus(CREATED);
        deviceRepository.save(device);

        ApplicationMapping<DeviceResponsePayload, Device> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(device, DeviceResponsePayload.class);
    }

    public DeviceResponsePayload updateDevice(String deviceUid, DeviceUpdateRequestPayload requestPayload) {
        logger.info("In updateDevice()");
        Optional<Device> deviceOpt = Optional.empty();
        if (!StringUtils.isEmpty(requestPayload.getOrganizationCode())) {
            if (!StringUtils.isEmpty(requestPayload.getDepartmentCode())) {
                if (!StringUtils.isEmpty(requestPayload.getTokenCategoryCode())) {
                    if (!StringUtils.isEmpty(requestPayload.getTokenTypeCode())) {
                        deviceOpt = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndDeviceUid(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(), requestPayload.getTokenCategoryCode(), requestPayload.getTokenTypeCode(), deviceUid);
                    } else {
                        deviceOpt = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndDeviceUid(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(), requestPayload.getTokenCategoryCode(), deviceUid);
                    }
                } else {
                    deviceOpt = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndDeviceUid(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(), deviceUid);
                }
            } else {
                deviceOpt = deviceRepository.findByOrganizationCodeAndDeviceUid(requestPayload.getOrganizationCode(), deviceUid);
            }
        } else {
            deviceOpt = deviceRepository.findByDeviceUid(deviceUid);
        }
        Device device = new Device();
        if (deviceOpt.isPresent()) {
            device = deviceOpt.get();
            device.setStatus(requestPayload.getStatus());
        } else {
            throw new ResourceNotFoundException("TS908- Invalid input, supplied data does not exists");
        }
        device.setDeviceUid(requestPayload.getDeviceUid());
        deviceRepository.save(device);

        ApplicationMapping<DeviceResponsePayload, Device> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(device, DeviceResponsePayload.class);
    }

    public String deleteDevice(String stageId) {
        logger.info("In deleteDevice()");
        Device device = deviceRepository.findById(stageId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid Device to delete"));
        device.setStatus(DELETED);
        deviceRepository.save(device);
        return "Device Deleted successfully";
    }
}
