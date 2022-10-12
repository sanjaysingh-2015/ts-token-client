package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.rule.ProcessStatusCreateRequestPayload;
import com.techsophy.tstokens.dto.rule.ProcessStatusResponsePayload;
import com.techsophy.tstokens.dto.rule.ProcessStatusUpdateRequestPayload;
import com.techsophy.tstokens.entity.ProcessStatus;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.*;
import com.techsophy.tstokens.utils.ApplicationMapping;
import com.techsophy.tstokens.utils.SecurityUtils;
import com.techsophy.tstokens.utils.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProcessStatusService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final ValidationUtils validationUtils;
    private final TokenCategoryService tokenCategoryService;
    private final TokenTypeRepository tokenTypeRepository;
    private final ProcessStatusRepository processStatusRepository;
    private static final String CREATED = "ACTIVE";
    private static final String DELETED = "DELETED";

    @Autowired
    public ProcessStatusService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, ValidationUtils validationUtils, TokenCategoryService tokenCategoryService, TokenTypeRepository tokenTypeRepository, ProcessStatusRepository processStatusRepository, TokenCategoryRepository tokenCategoryRepository) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.validationUtils = validationUtils;
        this.tokenCategoryService = tokenCategoryService;
        this.tokenTypeRepository = tokenTypeRepository;
        this.processStatusRepository = processStatusRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
    }

    public ProcessStatusResponsePayload getProcessStatusDetails(String orgCode, String deptCode, String catCode, String tokenTypeCode, String stageCode) {
        logger.info("In getProcessStatusDetails()");
        //TODO: Validation
        Optional<ProcessStatus> processStatusOpt = Optional.empty();
        if (!StringUtils.isEmpty(orgCode)) {
            if (!StringUtils.isEmpty(deptCode)) {
                if (!StringUtils.isEmpty(catCode)) {
                    if (!StringUtils.isEmpty(tokenTypeCode)) {
                        processStatusOpt = processStatusRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(orgCode, deptCode, catCode, tokenTypeCode, stageCode);
                    } else {
                        processStatusOpt = processStatusRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(orgCode, deptCode, catCode, stageCode);
                    }
                } else {
                    processStatusOpt = processStatusRepository.findByOrganizationCodeAndDepartmentCodeAndCode(orgCode, deptCode, stageCode);
                }
            } else {
                processStatusOpt = processStatusRepository.findByOrganizationCodeAndCode(orgCode, stageCode);
            }
        } else {
            processStatusOpt = processStatusRepository.findByCode(stageCode);
        }
        ProcessStatusResponsePayload response = null;
        ApplicationMapping<ProcessStatusResponsePayload, ProcessStatus> responseMapping = new ApplicationMapping<>();
        if (processStatusOpt.isPresent()) {
            response = responseMapping.convert(processStatusOpt.get(), ProcessStatusResponsePayload.class);
        } else {
            throw new ResourceNotFoundException("TS908- Invalid input, supplied data does not exists");
        }
        return response;
    }

    public List<ProcessStatusResponsePayload> getProcessStatusList(String orgCode, String deptCode, String catCode, String tokenTypeCode) {
        logger.info("In getProcessStatusDetails()");
        List<ProcessStatus> processStatusList;
        if (!StringUtils.isEmpty(orgCode)) {
            if (!StringUtils.isEmpty(deptCode)) {
                if (!StringUtils.isEmpty(catCode)) {
                    if (!StringUtils.isEmpty(tokenTypeCode)) {
                        processStatusList = processStatusRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(orgCode, deptCode, catCode, tokenTypeCode);
                    } else {
                        processStatusList = processStatusRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(orgCode, deptCode, catCode);
                    }
                } else {
                    processStatusList = processStatusRepository.findByOrganizationCodeAndDepartmentCode(orgCode, deptCode);
                }
            } else {
                processStatusList = processStatusRepository.findByOrganizationCode(orgCode);
            }
        } else {
            processStatusList = processStatusRepository.findAll();
        }
        List<ProcessStatusResponsePayload> response = new ArrayList<>();
        ApplicationMapping<ProcessStatusResponsePayload, ProcessStatus> responseMapping = new ApplicationMapping<>();
        if (!processStatusList.isEmpty()) {
            processStatusList.forEach(processStatus ->
                    response.add(responseMapping.convert(processStatus, ProcessStatusResponsePayload.class))
            );
        } else {
            throw new ResourceNotFoundException("TS908- Invalid input, supplied data does not exists");
        }
        return response;
    }

    public ProcessStatusResponsePayload createProcessStatus(ProcessStatusCreateRequestPayload requestPayload) {
        logger.info("In createProcessStatus()");

        Map<String, String> errors = new HashMap<>();
        //validationUtils.validateDepartment(requestPayload, errors);
        ApplicationMapping<ProcessStatus, ProcessStatusCreateRequestPayload> mapping = new ApplicationMapping<>();
        ProcessStatus processStatus = mapping.convert(requestPayload, ProcessStatus.class);
        return saveProcessStatus(processStatus);
    }

    public ProcessStatusResponsePayload saveProcessStatus(ProcessStatus processStatus) {
        logger.info("In saveProcessStatus()");
        organizationRepository.findByCodeAndStatus(processStatus.getOrganizationCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Organization Code Provided"));
        departmentRepository.findByOrganizationCodeAndCodeAndStatus(processStatus.getOrganizationCode(), processStatus.getDepartmentCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Department Code Provided"));
        tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCodeAndStatus(processStatus.getOrganizationCode(), processStatus.getDepartmentCode(), processStatus.getTokenCategoryCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Category Code Provided"));
        tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCodeAndStatus(processStatus.getOrganizationCode(), processStatus.getDepartmentCode(), processStatus.getTokenCategoryCode(), processStatus.getTokenTypeCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Token Type Code Provided"));
        processStatus.setCreatedOn(new Date());
        processStatus.setStatus(CREATED);
        processStatus.setCode(SecurityUtils.generateCode(processStatus.getName(), 4));
        processStatusRepository.save(processStatus);

        ApplicationMapping<ProcessStatusResponsePayload, ProcessStatus> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(processStatus, ProcessStatusResponsePayload.class);
    }

    public ProcessStatusResponsePayload updateProcessStatus(String stageCode, ProcessStatusUpdateRequestPayload requestPayload) {
        logger.info("In updateProcessStatus()");
        Optional<ProcessStatus> processStatusOpt = Optional.empty();
        if (!StringUtils.isEmpty(requestPayload.getOrganizationCode())) {
            if (!StringUtils.isEmpty(requestPayload.getDepartmentCode())) {
                if (!StringUtils.isEmpty(requestPayload.getTokenCategoryCode())) {
                    if (!StringUtils.isEmpty(requestPayload.getTokenTypeCode())) {
                        processStatusOpt = processStatusRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(), requestPayload.getTokenCategoryCode(), requestPayload.getTokenTypeCode(), stageCode);
                    } else {
                        processStatusOpt = processStatusRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(), requestPayload.getTokenCategoryCode(), stageCode);
                    }
                } else {
                    processStatusOpt = processStatusRepository.findByOrganizationCodeAndDepartmentCodeAndCode(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(), stageCode);
                }
            } else {
                processStatusOpt = processStatusRepository.findByOrganizationCodeAndCode(requestPayload.getOrganizationCode(), stageCode);
            }
        } else {
            processStatusOpt = processStatusRepository.findByCode(stageCode);
        }
        ProcessStatus processStatus = new ProcessStatus();
        if (processStatusOpt.isPresent()) {
            processStatus = processStatusOpt.get();
            processStatus.setStatus(requestPayload.getStatus());
        } else {
            throw new ResourceNotFoundException("TS908- Invalid input, supplied data does not exists");
        }
        processStatus.setName(requestPayload.getName());
        processStatusRepository.save(processStatus);

        ApplicationMapping<ProcessStatusResponsePayload, ProcessStatus> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(processStatus, ProcessStatusResponsePayload.class);
    }

    public String deleteProcessStatus(String stageId) {
        logger.info("In deleteProcessStatus()");
        ProcessStatus processStatus = processStatusRepository.findById(stageId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid Process Status to delete"));
        processStatus.setStatus(DELETED);
        processStatusRepository.save(processStatus);
        return "Process Status Deleted successfully";
    }
}
