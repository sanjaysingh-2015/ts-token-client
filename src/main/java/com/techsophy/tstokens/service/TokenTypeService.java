package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.org.TokenTypeCreateRequestPayload;
import com.techsophy.tstokens.dto.org.TokenTypeResponsePayload;
import com.techsophy.tstokens.dto.org.TokenTypeUpdateRequestPayload;
import com.techsophy.tstokens.entity.TokenCategory;
import com.techsophy.tstokens.entity.TokenType;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.DepartmentRepository;
import com.techsophy.tstokens.repository.OrganizationRepository;
import com.techsophy.tstokens.repository.TokenCategoryRepository;
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
public class TokenTypeService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final ValidationUtils validationUtils;

    private static final String CREATED = "ACTIVE";
    private static final String DELETED = "DELETED";

    @Autowired
    public TokenTypeService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, TokenCategoryRepository tokenCategoryRepository, TokenTypeRepository tokenTypeRepository, ValidationUtils validationUtils) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
        this.tokenTypeRepository = tokenTypeRepository;
        this.validationUtils = validationUtils;
    }

    public TokenTypeResponsePayload getTokenTypeDetails(String tokenCatCode, String tokenTypeCode) {
        logger.info("In getTokenCategoryDetails()");
        Optional<TokenType> tokenTypeOpt = tokenTypeRepository.findByTokenCategoryCodeAndCode(tokenCatCode, tokenTypeCode);
        TokenTypeResponsePayload response = null;
        ApplicationMapping<TokenTypeResponsePayload, TokenType> responseMapping = new ApplicationMapping<>();
        if (tokenTypeOpt.isPresent()) {
            response = responseMapping.convert(tokenTypeOpt.get(), TokenTypeResponsePayload.class);
        }
        return response;
    }

    public List<TokenTypeResponsePayload> getTokenTypeList(String tokenCatCode) {
        logger.info("In getDepartmentList()");
        List<TokenType> tokenTypeList = tokenTypeRepository.findByTokenCategoryCode(tokenCatCode);
        List<TokenTypeResponsePayload> response = new ArrayList<>();
        ApplicationMapping<TokenTypeResponsePayload, TokenType> responseMapping = new ApplicationMapping<>();
        tokenTypeList.forEach(tokenType ->
                response.add(responseMapping.convert(tokenType, TokenTypeResponsePayload.class))
        );
        return response;
    }

    public TokenTypeResponsePayload createTokenType(String tokenCatCode, TokenTypeCreateRequestPayload requestPayload) {
        logger.info("In createTokenType()");
        Optional<TokenCategory> tokenCategoryOpt = tokenCategoryRepository.findByCode(tokenCatCode);
        if (tokenCategoryOpt.isEmpty()) {
            throw new ResourceNotFoundException("TOken Category does not exists with this code");
        }
        Map<String, String> errors = new HashMap<>();
        //validationUtils.validateTokenType(tokenCatCode,requestPayload, errors);
        ApplicationMapping<TokenType, TokenTypeCreateRequestPayload> mapping = new ApplicationMapping<>();
        TokenType tokenType = mapping.convert(requestPayload, TokenType.class);
        return saveTokenType(tokenCategoryOpt.get(), tokenType);
    }

    public TokenTypeResponsePayload saveTokenType(TokenCategory tokenCategory, TokenType tokenType) {
        logger.info("In saveTokenType()");
        tokenType.setOrganizationCode(tokenCategory.getOrganizationCode());
        tokenType.setDepartmentCode(tokenCategory.getDepartmentCode());
        tokenType.setTokenCategoryCode(tokenCategory.getCode());
        tokenType.setCreatedOn(new Date());
        tokenType.setStatus(CREATED);
        tokenType.setCode(SecurityUtils.generateCode(tokenCategory.getName(), 4));
        tokenTypeRepository.save(tokenType);

        ApplicationMapping<TokenTypeResponsePayload, TokenType> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(tokenType, TokenTypeResponsePayload.class);
    }

    public TokenTypeResponsePayload updateTokenType(String tokenCatCode, String tokenTypeCode, TokenTypeUpdateRequestPayload requestPayload) {
        logger.info("In updateTokenType()");
        Optional<TokenType> tokenTypeOpt;
        if (!StringUtils.isEmpty(tokenCatCode)) {
            tokenTypeOpt = tokenTypeRepository.findByTokenCategoryCodeAndCode(tokenCatCode, tokenTypeCode);
            if (tokenTypeOpt.isEmpty()) {
                throw new ResourceNotFoundException("Invalid Token Type to update");
            }
        } else {
            tokenTypeOpt = tokenTypeRepository.findByTokenCategoryCodeAndCode(tokenCatCode, tokenTypeCode);
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

    public String deleteTokenType(String typeId) {
        logger.info("In deleteDepartment()");
        TokenType tokenType = tokenTypeRepository.findById(typeId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid Organization to delete"));
        tokenType.setStatus(DELETED);
        tokenType.setUpdatedOn(new Date());
        tokenTypeRepository.save(tokenType);
        return "Token Type Deleted successfully";
    }
}
