package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.common.MasterLookupItemPayload;
import com.techsophy.tstokens.dto.common.MasterLookupPayload;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MasterLookupService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final CounterRepository counterRepository;
    private final ProcessStageRepository processStageRepository;
    private final ProcessStatusRepository processStatusRepository;
    private final DeviceRepository deviceRepository;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public MasterLookupService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, TokenCategoryRepository tokenCategoryRepository, TokenTypeRepository tokenTypeRepository, CounterRepository counterRepository, ProcessStageRepository processStageRepository, ProcessStatusRepository processStatusRepository, DeviceRepository deviceRepository, MongoTemplate mongoTemplate) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
        this.tokenTypeRepository = tokenTypeRepository;
        this.counterRepository = counterRepository;
        this.processStageRepository = processStageRepository;
        this.processStatusRepository = processStatusRepository;
        this.deviceRepository = deviceRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public MasterLookupPayload getMasterLookupData(String lookupType, String orgCode, String deptCode, String catCode, String typeCode) {
        logger.info("In getMasterLookupData()");
        MasterLookupPayload response = new MasterLookupPayload();
        switch(lookupType) {
            case "ORG":
                List<MasterLookupItemPayload> organizations = new ArrayList<>();
                var orgResult = organizationRepository.findAll();
                if(orgResult != null) {
                    orgResult.forEach(item -> organizations.add(new MasterLookupItemPayload(item.getCode(), item.getName())));
                }
                response.setOrganizations(organizations);
                break;
            case "DEPT":
                List<MasterLookupItemPayload> departments = new ArrayList<>();
                var deptResult = departmentRepository.findByOrganizationCode(orgCode);
                if(deptResult != null) {
                    deptResult.forEach(item -> departments.add(new MasterLookupItemPayload(item.getCode(), item.getName())));
                }
                response.setDepartments(departments);
                break;
            case "CAT":
                List<MasterLookupItemPayload> categories = new ArrayList<>();
                var catResult = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCode(orgCode, deptCode);
                if(catResult != null) {
                    catResult.forEach(item -> categories.add(new MasterLookupItemPayload(item.getCode(), item.getName())));
                }
                response.setCategories(categories);
                break;
            case "TYP":
                List<MasterLookupItemPayload> tokenTypes = new ArrayList<>();
                var typeResult = tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(orgCode, deptCode, catCode);
                if(typeResult != null) {
                    typeResult.forEach(item -> tokenTypes.add(new MasterLookupItemPayload(item.getCode(), item.getName())));
                }
                response.setTokenTypes(tokenTypes);
                break;
            case "CNT":
                List<MasterLookupItemPayload> counters = new ArrayList<>();
                var cntResult = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(orgCode, deptCode, catCode, typeCode);
                if(cntResult != null) {
                    cntResult.forEach(item -> counters.add(new MasterLookupItemPayload(item.getCode(), item.getCounterNo())));
                }
                response.setCounters(counters);
                break;
            case "STG":
                List<MasterLookupItemPayload> processStages = new ArrayList<>();
                var stgResult = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(orgCode, deptCode, catCode, typeCode);
                if(stgResult != null) {
                    stgResult.forEach(item -> processStages.add(new MasterLookupItemPayload(item.getCode(), item.getName())));
                }
                response.setProcessStages(processStages);
                break;
            case "DVC":
                List<MasterLookupItemPayload> devices = new ArrayList<>();
                var dvcResult = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(orgCode, deptCode, catCode, typeCode);
                if(dvcResult != null) {
                    dvcResult.forEach(item -> devices.add(new MasterLookupItemPayload(item.getDeviceUid(), item.getDeviceName())));
                }
                response.setDevices(devices);
                break;
            case "ALL":
                organizations = new ArrayList<>();
                orgResult = organizationRepository.findAll();
                if(orgResult != null) {
                    orgResult.forEach(item -> organizations.add(new MasterLookupItemPayload(item.getCode(), item.getName())));
                }
                response.setOrganizations(organizations);
                if(!StringUtils.isEmpty(orgCode)) {
                    departments = new ArrayList<>();
                    deptResult = departmentRepository.findByOrganizationCode(orgCode);
                    if (deptResult != null) {
                        deptResult.forEach(item -> departments.add(new MasterLookupItemPayload(item.getCode(), item.getName())));
                    }
                    response.setDepartments(departments);
                    if(!StringUtils.isEmpty(deptCode)) {
                        categories = new ArrayList<>();
                        catResult = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCode(orgCode, deptCode);
                        if (catResult != null) {
                            catResult.forEach(item -> categories.add(new MasterLookupItemPayload(item.getCode(), item.getName())));
                        }
                        response.setCategories(categories);
                        if (!StringUtils.isEmpty(catCode)) {
                            tokenTypes = new ArrayList<>();
                            typeResult = tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(orgCode, deptCode, catCode);
                            if (typeResult != null) {
                                typeResult.forEach(item -> tokenTypes.add(new MasterLookupItemPayload(item.getCode(), item.getName())));
                            }
                            response.setTokenTypes(tokenTypes);
                        }
                    }
                    counters = new ArrayList<>();
                    cntResult = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(orgCode, deptCode, catCode, typeCode);
                    if (cntResult != null) {
                        cntResult.forEach(item -> counters.add(new MasterLookupItemPayload(item.getCode(), item.getCounterNo())));
                    }
                    response.setCounters(counters);
                    processStages = new ArrayList<>();
                    stgResult = processStageRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(orgCode, deptCode, catCode, typeCode);
                    if (stgResult != null) {
                        stgResult.forEach(item -> processStages.add(new MasterLookupItemPayload(item.getCode(), item.getName())));
                    }
                    response.setProcessStages(processStages);
                    devices = new ArrayList<>();
                    dvcResult = deviceRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(orgCode, deptCode, catCode, typeCode);
                    if (dvcResult != null) {
                        dvcResult.forEach(item -> devices.add(new MasterLookupItemPayload(item.getDeviceUid(), item.getDeviceName())));
                    }
                    response.setDevices(devices);
                }
                break;
            default:
                throw new ResourceNotFoundException("Wrong Master lookup");
        }
        return response;
    }

}
