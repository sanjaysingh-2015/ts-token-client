package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.org.*;
import com.techsophy.tstokens.entity.Department;
import com.techsophy.tstokens.entity.Organization;
import com.techsophy.tstokens.entity.TokenCategory;
import com.techsophy.tstokens.entity.TokenType;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.DepartmentRepository;
import com.techsophy.tstokens.repository.OrganizationRepository;
import com.techsophy.tstokens.repository.TokenCategoryRepository;
import com.techsophy.tstokens.repository.TokenTypeRepository;
import com.techsophy.tstokens.utils.ApplicationMapping;
import com.techsophy.tstokens.utils.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final ValidationUtils validationUtils;

    private static final String CREATED = "CREATED";
    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, TokenCategoryRepository tokenCategoryRepository, TokenTypeRepository tokenTypeRepository, ValidationUtils validationUtils) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
        this.tokenTypeRepository = tokenTypeRepository;
        this.validationUtils = validationUtils;
    }

    public OrganizationResponsePayload getOrganizationDetails(String orgCode) {
        logger.info("In getOrganizationDetails()");
        Optional<Organization> organizationOpt = organizationRepository.findByCode(orgCode);
        OrganizationResponsePayload response = null;
        ApplicationMapping<OrganizationResponsePayload, Organization> responseMapping = new ApplicationMapping<>();
        if(organizationOpt.isPresent()) {
            response = responseMapping.convert(organizationOpt.get(), OrganizationResponsePayload.class);
        }
        return response;
    }
    public List<OrganizationResponsePayload> getOrganizationList() {
        logger.info("In getOrganizationList()");
        List<Organization> organizationList = organizationRepository.findAll();
        List<OrganizationResponsePayload> response = new ArrayList<>();
        ApplicationMapping<OrganizationResponsePayload, Organization> responseMapping = new ApplicationMapping<>();
        organizationList.forEach(organization ->
            response.add(responseMapping.convert(organization, OrganizationResponsePayload.class))
        );
        return response;
    }
    public OrganizationResponsePayload createOrganization(OrganizationCreateRequestPayload requestPayload) {
        logger.info("In createOrganization()");
        validationUtils.validateOrganization(requestPayload);
        ApplicationMapping<Organization, OrganizationCreateRequestPayload> mapping = new ApplicationMapping<>();
        Organization organization = mapping.convert(requestPayload, Organization.class);
        return saveOrganization(organization);
    }
    public OrganizationResponsePayload saveOrganization(Organization organization) {
        logger.info("In saveOrganization()");
        organization.setCreatedOn(new Date());
        organization.setStatus(CREATED);
        if (organization.getDepartments() != null) {
            for (Department department : organization.getDepartments()) {
                saveDepartment(organization, department);
            }
        }
        organization = organizationRepository.save(organization);
        ApplicationMapping<OrganizationResponsePayload, Organization> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(organization, OrganizationResponsePayload.class);
    }
    public DepartmentResponsePayload saveDepartment(Organization organization, Department department) {
        logger.info("In saveDepartment()");
        department.setOrganizationCode(organization.getCode());
        department.setCreatedOn(new Date());
        department.setStatus(CREATED);
        departmentRepository.save(department);
        if (department.getTokenCategories() != null) {
            for (TokenCategory tokenCategory : department.getTokenCategories()) {
                saveTokenCategory(organization, department, tokenCategory);
            }
        }
        ApplicationMapping<DepartmentResponsePayload, Department> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(department, DepartmentResponsePayload.class);
    }
    public TokenCategoryResponsePayload saveTokenCategory(Organization organization, Department department, TokenCategory tokenCategory) {
        logger.info("In saveTokenCategory()");
        tokenCategory.setOrganizationCode(organization.getCode());
        tokenCategory.setDepartmentCode(department.getCode());
        tokenCategory.setCreatedOn(new Date());
        tokenCategory.setStatus(CREATED);
        tokenCategoryRepository.save(tokenCategory);
        if (tokenCategory.getTokenTypes() != null) {
            for (TokenType tokenType : tokenCategory.getTokenTypes()) {
                saveTokenType(organization,department,tokenCategory,tokenType);
            }
        }
        ApplicationMapping<TokenCategoryResponsePayload, TokenCategory> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(tokenCategory, TokenCategoryResponsePayload.class);
    }
    public TokenTypeResponsePayload saveTokenType(Organization organization, Department department, TokenCategory tokenCategory, TokenType tokenType) {
        logger.info("In saveTokenType()");
        tokenType.setOrganizationCode(organization.getCode());
        tokenType.setDepartmentCode(department.getCode());
        tokenType.setTokenCategoryCode(tokenCategory.getCode());
        tokenType.setCreatedOn(new Date());
        tokenType.setStatus(CREATED);
        tokenTypeRepository.save(tokenType);

        ApplicationMapping<TokenTypeResponsePayload, TokenType> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(tokenType, TokenTypeResponsePayload.class);
    }
    public OrganizationResponsePayload updateOrganization(String orgCode, OrganizationUpdateRequestPayload requestPayload) {
        logger.info("In updateOrganization()");
        Optional<Organization> organizationOpt = organizationRepository.findByCode(orgCode);
        if (organizationOpt.isEmpty()) {
            throw new ResourceNotFoundException("Invalid Organization to update");
        }
        Organization organization = organizationOpt.get();
        organization.setName(requestPayload.getName());
        organization.setAddress(requestPayload.getAddress());
        organization.setCity(requestPayload.getCity());
        organization.setState(requestPayload.getState());
        organization.setCountry(requestPayload.getCountry());
        organization.setTokenPrefix(requestPayload.getTokenPrefix());
        organization.setStatus(requestPayload.getStatus());
        organization.setUpdatedOn(new Date());
        organizationRepository.save(organization);
        if (requestPayload.getDepartments() != null) {
            for(DepartmentUpdateRequestPayload department : requestPayload.getDepartments()) {
                updateDepartment(orgCode, "", department);
            }
        }
        ApplicationMapping<OrganizationResponsePayload, Organization> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(organization, OrganizationResponsePayload.class);
    }
    public void updateDepartment(String orgCode, String deptCode, DepartmentUpdateRequestPayload requestPayload) {
        logger.info("In updateDepartment()");
        Optional<Department> departmentOpt;
        if(!StringUtils.isEmpty(deptCode)) {
            departmentOpt = departmentRepository.findByOrganizationCodeAndCode(orgCode, deptCode);
            if(departmentOpt.isEmpty()) {
                throw new ResourceNotFoundException("Invalid Department to update");
            }
        } else {
            departmentOpt = departmentRepository.findByOrganizationCodeAndCode(orgCode, requestPayload.getCode());
        }
        Department department = new Department();
        if(departmentOpt.isPresent()) {
            department = departmentOpt.get();
            department.setStatus(requestPayload.getStatus());
            department.setUpdatedOn(new Date());
        } else {
            department.setStatus(CREATED);
            department.setCreatedOn(new Date());
        }
        department.setName(requestPayload.getName());
        department.setTokenPrefix(requestPayload.getTokenPrefix());
        departmentRepository.save(department);
        if (requestPayload.getTokenCategories() != null) {
            for(TokenCategoryUpdateRequestPayload tokenCategory : requestPayload.getTokenCategories()) {
                updateTokenCategory(orgCode, department.getCode(), "", tokenCategory);
            }
        }
    }
    public void updateTokenCategory(String orgCode, String deptCode, String tokenCatCode, TokenCategoryUpdateRequestPayload requestPayload) {
        logger.info("In updateTokenCategory()");
        Optional<TokenCategory> tokenCategoryOpt;
        if(!StringUtils.isEmpty(tokenCatCode)) {
            tokenCategoryOpt = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(orgCode, deptCode, tokenCatCode);
            if(tokenCategoryOpt.isEmpty()) {
                throw new ResourceNotFoundException("Invalid Token Category to update");
            }
        } else {
            tokenCategoryOpt = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(orgCode, deptCode, requestPayload.getCode());
        }
        TokenCategory tokenCategory = new TokenCategory();
        if(tokenCategoryOpt.isPresent()) {
            tokenCategory = tokenCategoryOpt.get();
            tokenCategory.setStatus(requestPayload.getStatus());
            tokenCategory.setUpdatedOn(new Date());
        } else {
            tokenCategory.setStatus(CREATED);
            tokenCategory.setCreatedOn(new Date());
        }
        tokenCategory.setName(requestPayload.getName());
        tokenCategory.setTokenPrefix(requestPayload.getTokenPrefix());
        tokenCategoryRepository.save(tokenCategory);
        if (requestPayload.getTokenTypes() != null) {
            for(TokenTypeUpdateRequestPayload tokenType : requestPayload.getTokenTypes()) {
                updateTokenType(orgCode, deptCode, tokenCategory.getCode(), "", tokenType);
            }
        }
    }
    public void updateTokenType(String orgCode, String deptCode, String tokenCatCode, String tokenTypeCode, TokenTypeUpdateRequestPayload requestPayload) {
        logger.info("In updateTokenType()");
        Optional<TokenType> tokenTypeOpt;
        if (!StringUtils.isEmpty(tokenCatCode)) {
            tokenTypeOpt = tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(orgCode, deptCode, tokenCatCode, tokenTypeCode);
            if (tokenTypeOpt.isEmpty()) {
                throw new ResourceNotFoundException("Invalid Token Type to update");
            }
        } else {
            tokenTypeOpt = tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(orgCode, deptCode, tokenCatCode, requestPayload.getCode());
        }
        TokenType tokenType = new TokenType();
        if (tokenTypeOpt.isPresent()) {
            tokenType = tokenTypeOpt.get();
            tokenType.setStatus(requestPayload.getStatus());
            tokenType.setUpdatedOn(new Date());
        } else {
            tokenType.setStatus(CREATED);
            tokenType.setCreatedOn(new Date());
        }
        tokenType.setName(requestPayload.getName());
        tokenType.setTokenPrefix(requestPayload.getTokenPrefix());
        tokenTypeRepository.save(tokenType);
    }
}