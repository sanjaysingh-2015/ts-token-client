package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.rule.ProcessStageCreateRequestPayload;
import com.techsophy.tstokens.dto.rule.ProcessStageResponsePayload;
import com.techsophy.tstokens.dto.rule.ProcessStageUpdateRequestPayload;
import com.techsophy.tstokens.entity.ProcessStage;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.DepartmentRepository;
import com.techsophy.tstokens.repository.OrganizationRepository;
import com.techsophy.tstokens.repository.ProcessStageRepository;
import com.techsophy.tstokens.repository.TokenTypeRepository;
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
public class ProcessStageService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final ValidationUtils validationUtils;
    private final TokenCategoryService tokenCategoryService;
    private final TokenTypeRepository tokenTypeRepository;
    private final ProcessStageRepository processStageRepository;
    private static final String CREATED = "ACTIVE";
    private static final String DELETED = "DELETED";

    @Autowired
    public ProcessStageService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, ValidationUtils validationUtils, TokenCategoryService tokenCategoryService, TokenTypeRepository tokenTypeRepository, ProcessStageRepository processStageRepository) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.validationUtils = validationUtils;
        this.tokenCategoryService = tokenCategoryService;
        this.tokenTypeRepository = tokenTypeRepository;
        this.processStageRepository = processStageRepository;
    }

    public ProcessStageResponsePayload getProcessStageDetails(String orgCode, String deptCode, String catCode, String tokenTypeCode, String stageCode) {
        logger.info("In getProcessStageDetails()");
        //TODO: Validation
        Optional<ProcessStage> processStageOpt = Optional.empty();
        if(!StringUtils.isEmpty(orgCode)) {
            if(!StringUtils.isEmpty(deptCode)) {
                if(!StringUtils.isEmpty(catCode)) {
                    if(!StringUtils.isEmpty(tokenTypeCode)) {
                        processStageOpt = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(orgCode, deptCode, catCode, tokenTypeCode,stageCode);
                    } else {
                        processStageOpt = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(orgCode, deptCode, catCode,stageCode);
                    }
                } else {
                    processStageOpt = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndCode(orgCode, deptCode,stageCode);
                }
            } else {
                processStageOpt = processStageRepository.findByOrganizationCodeAndCode(orgCode,stageCode);
            }
        } else {
            processStageOpt = processStageRepository.findByCode(stageCode);
        }
        ProcessStageResponsePayload response = null;
        ApplicationMapping<ProcessStageResponsePayload, ProcessStage> responseMapping = new ApplicationMapping<>();
        if(processStageOpt.isPresent()) {
            response = responseMapping.convert(processStageOpt.get(), ProcessStageResponsePayload.class);
        }
        return response;
    }
    public List<ProcessStageResponsePayload> getProcessStageList(String orgCode, String deptCode, String catCode, String tokenTypeCode) {
        logger.info("In getProcessStageDetails()");
        List<ProcessStage> processStageList;
        if(!StringUtils.isEmpty(orgCode)) {
            if(!StringUtils.isEmpty(deptCode)) {
                if(!StringUtils.isEmpty(catCode)) {
                    if(!StringUtils.isEmpty(tokenTypeCode)) {
                        processStageList = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(orgCode, deptCode, catCode, tokenTypeCode);
                    } else {
                        processStageList = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(orgCode, deptCode, catCode);
                    }
                } else {
                    processStageList = processStageRepository.findByOrganizationCodeAndDepartmentCode(orgCode, deptCode);
                }
            } else {
                processStageList = processStageRepository.findByOrganizationCode(orgCode);
            }
        } else {
            processStageList = processStageRepository.findAll();
        }
        List<ProcessStageResponsePayload> response = new ArrayList<>();
        ApplicationMapping<ProcessStageResponsePayload, ProcessStage> responseMapping = new ApplicationMapping<>();
        processStageList.forEach(processStage ->
                response.add(responseMapping.convert(processStage, ProcessStageResponsePayload.class))
        );
        return response;
    }
    public ProcessStageResponsePayload createProcessStage(ProcessStageCreateRequestPayload requestPayload) {
        logger.info("In createProcessStage()");

        Map<String, String> errors = new HashMap<>();
        //validationUtils.validateDepartment(requestPayload, errors);
        ApplicationMapping<ProcessStage, ProcessStageCreateRequestPayload> mapping = new ApplicationMapping<>();
        ProcessStage processStage = mapping.convert(requestPayload, ProcessStage.class);
        return saveProcessStage(processStage);
    }
    public ProcessStageResponsePayload saveProcessStage(ProcessStage processStage) {
        logger.info("In saveProcessStage()");
        processStage.setCreatedOn(new Date());
        processStage.setStatus(CREATED);
        processStage.setCode(SecurityUtils.generateCode(processStage.getName(),4));
        processStageRepository.save(processStage);

        ApplicationMapping<ProcessStageResponsePayload, ProcessStage> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(processStage, ProcessStageResponsePayload.class);
    }
    public ProcessStageResponsePayload updateProcessStage(String stageCode, ProcessStageUpdateRequestPayload requestPayload) {
        logger.info("In updateProcessStage()");
        Optional<ProcessStage> processStageOpt = Optional.empty();
        if(!StringUtils.isEmpty(requestPayload.getOrganizationCode())) {
            if(!StringUtils.isEmpty(requestPayload.getDepartmentCode())) {
                if(!StringUtils.isEmpty(requestPayload.getTokenCategoryCode())) {
                    if(!StringUtils.isEmpty(requestPayload.getTokenTypeCode())) {
                        processStageOpt = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(), requestPayload.getTokenCategoryCode(), requestPayload.getTokenTypeCode(), stageCode);
                    } else {
                        processStageOpt = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(), requestPayload.getTokenCategoryCode(),stageCode);
                    }
                } else {
                    processStageOpt = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndCode(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(),stageCode);
                }
            } else {
                processStageOpt = processStageRepository.findByOrganizationCodeAndCode(requestPayload.getOrganizationCode(),stageCode);
            }
        } else {
            processStageOpt = processStageRepository.findByCode(stageCode);
        }
        ProcessStage processStage = new ProcessStage();
        if(processStageOpt.isPresent()) {
            processStage = processStageOpt.get();
            processStage.setStatus(requestPayload.getStatus());
        }
        processStage.setName(requestPayload.getName());
        processStageRepository.save(processStage);

        ApplicationMapping<ProcessStageResponsePayload, ProcessStage> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(processStage, ProcessStageResponsePayload.class);
    }
    public String deleteProcessStage(String stageId) {
        logger.info("In deleteProcessStage()");
        ProcessStage processStage = processStageRepository.findById(stageId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid Process Stage to delete"));
        processStage.setStatus(DELETED);
        processStageRepository.save(processStage);
        return "Process Stage Deleted successfully";
    }

}
