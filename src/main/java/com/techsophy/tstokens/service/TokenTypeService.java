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

import java.util.*;

@Service
public class TokenTypeService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final ValidationUtils validationUtils;

    private static final String CREATED = "CREATED";

    @Autowired
    public TokenTypeService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, TokenCategoryRepository tokenCategoryRepository, TokenTypeRepository tokenTypeRepository, ValidationUtils validationUtils) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
        this.tokenTypeRepository = tokenTypeRepository;
        this.validationUtils = validationUtils;
    }

    public TokenTypeResponsePayload getTokenTypeDetails(String orgCode, String deptCode, String tokenCatCode, String tokenTypeCode) {
        logger.info("In getTokenCategoryDetails()");
        Optional<TokenType> tokenTypeOpt = tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(orgCode, deptCode, tokenCatCode, tokenTypeCode);
        TokenTypeResponsePayload response = null;
        ApplicationMapping<TokenTypeResponsePayload, TokenType> responseMapping = new ApplicationMapping<>();
        if(tokenTypeOpt.isPresent()) {
            response = responseMapping.convert(tokenTypeOpt.get(), TokenTypeResponsePayload.class);
        }
        return response;
    }

    public List<TokenTypeResponsePayload> getTokenTypeList(String orgCode, String deptCode, String tokenCatCode) {
        logger.info("In getDepartmentList()");
        List<TokenType> tokenTypeList = tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(orgCode, deptCode, tokenCatCode);
        List<TokenTypeResponsePayload> response = new ArrayList<>();
        ApplicationMapping<TokenTypeResponsePayload, TokenType> responseMapping = new ApplicationMapping<>();
        tokenTypeList.forEach(tokenType ->
                response.add(responseMapping.convert(tokenType, TokenTypeResponsePayload.class))
        );
        return response;
    }

    public TokenTypeResponsePayload createTokenType(String orgCode, String deptCode, String tokenCatCode, TokenTypeCreateRequestPayload requestPayload) {
        logger.info("In createTokenType()");
        Optional<Organization> organizationOpt = organizationRepository.findByCode(orgCode);
        if(organizationOpt.isEmpty()) {
            throw new ResourceNotFoundException("Organization does not exists with this code");
        }
        Optional<Department> departmentOpt = departmentRepository.findByOrganizationCodeAndCode(orgCode, deptCode);
        if(departmentOpt.isEmpty()) {
            throw new ResourceNotFoundException("Department does not exists with this code");
        }
        Optional<TokenCategory> tokenCategoryOpt = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(orgCode, deptCode, tokenCatCode);
        if(tokenCategoryOpt.isEmpty()) {
            throw new ResourceNotFoundException("TOken Category does not exists with this code");
        }
        Map<String, String> errors = new HashMap<>();
        validationUtils.validateTokenType(orgCode,deptCode,tokenCatCode,requestPayload, errors);
        ApplicationMapping<TokenType, TokenTypeCreateRequestPayload> mapping = new ApplicationMapping<>();
        TokenType tokenType = mapping.convert(requestPayload, TokenType.class);
        return saveTokenType(organizationOpt.get(), departmentOpt.get(), tokenCategoryOpt.get(),tokenType);
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
    public TokenTypeResponsePayload updateTokenType(String orgCode, String deptCode, String tokenCatCode, String tokenTypeCode, TokenTypeUpdateRequestPayload requestPayload) {
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
        ApplicationMapping<TokenTypeResponsePayload, TokenType> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(tokenType, TokenTypeResponsePayload.class);
    }
}
