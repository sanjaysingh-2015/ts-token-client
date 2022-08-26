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
public class TokenCategoryService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final ValidationUtils validationUtils;
    private final TokenTypeService tokenTypeService;

    private static final String CREATED = "CREATED";

    @Autowired
    public TokenCategoryService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, TokenCategoryRepository tokenCategoryRepository, TokenTypeRepository tokenTypeRepository, ValidationUtils validationUtils, TokenTypeService tokenTypeService) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
        this.tokenTypeRepository = tokenTypeRepository;
        this.validationUtils = validationUtils;
        this.tokenTypeService = tokenTypeService;
    }

    public TokenCategoryResponsePayload getTokenCategoryDetails(String orgCode, String deptCode, String tokenCatCode) {
        logger.info("In getTokenCategoryDetails()");
        Optional<TokenCategory> tokenCategoryOpt = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(orgCode, deptCode, tokenCatCode);
        TokenCategoryResponsePayload response = null;
        ApplicationMapping<TokenCategoryResponsePayload, TokenCategory> responseMapping = new ApplicationMapping<>();
        if(tokenCategoryOpt.isPresent()) {
            response = responseMapping.convert(tokenCategoryOpt.get(), TokenCategoryResponsePayload.class);
        }
        return response;
    }

    public List<TokenCategoryResponsePayload> getTokenCategoryList(String orgCode, String deptCode) {
        logger.info("In getDepartmentList()");
        List<TokenCategory> tokenCategoryList = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCode(orgCode, deptCode);
        List<TokenCategoryResponsePayload> response = new ArrayList<>();
        ApplicationMapping<TokenCategoryResponsePayload, TokenCategory> responseMapping = new ApplicationMapping<>();
        tokenCategoryList.forEach(tokenCategory ->
                response.add(responseMapping.convert(tokenCategory, TokenCategoryResponsePayload.class))
        );
        return response;
    }

    public TokenCategoryResponsePayload createTokenCategory(String orgCode, String deptCode, TokenCategoryCreateRequestPayload requestPayload) {
        logger.info("In createOrganization()");
        Optional<Organization> organizationOpt = organizationRepository.findByCode(orgCode);
        if(organizationOpt.isEmpty()) {
            throw new ResourceNotFoundException("Organization does not exists with this code");
        }
        Optional<Department> departmentOpt = departmentRepository.findByOrganizationCodeAndCode(orgCode, deptCode);
        if(departmentOpt.isEmpty()) {
            throw new ResourceNotFoundException("Department does not exists with this code");
        }
        Map<String, String> errors = new HashMap<>();
        validationUtils.validateTokenCategory(orgCode,deptCode, requestPayload, errors);
        ApplicationMapping<TokenCategory, TokenCategoryCreateRequestPayload> mapping = new ApplicationMapping<>();
        TokenCategory tokenCategory = mapping.convert(requestPayload, TokenCategory.class);
        return saveTokenCategory(organizationOpt.get(), departmentOpt.get(), tokenCategory);
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
                tokenTypeService.saveTokenType(organization,department,tokenCategory,tokenType);
            }
        }
        ApplicationMapping<TokenCategoryResponsePayload, TokenCategory> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(tokenCategory, TokenCategoryResponsePayload.class);
    }
    public TokenCategoryResponsePayload updateTokenCategory(String orgCode, String deptCode, String tokenCatCode, TokenCategoryUpdateRequestPayload requestPayload) {
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
                tokenTypeService.updateTokenType(orgCode, deptCode, tokenCategory.getCode(), "", tokenType);
            }
        }
        ApplicationMapping<TokenCategoryResponsePayload, TokenCategory> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(tokenCategory, TokenCategoryResponsePayload.class);
    }
}
