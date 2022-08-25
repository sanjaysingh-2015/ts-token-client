package com.techsophy.tstokens.utils;

import com.techsophy.tstokens.dto.org.DepartmentCreateRequestPayload;
import com.techsophy.tstokens.dto.org.OrganizationCreateRequestPayload;
import com.techsophy.tstokens.dto.org.TokenCategoryCreateRequestPayload;
import com.techsophy.tstokens.dto.org.TokenTypeCreateRequestPayload;
import com.techsophy.tstokens.entity.Department;
import com.techsophy.tstokens.entity.Organization;
import com.techsophy.tstokens.entity.TokenCategory;
import com.techsophy.tstokens.entity.TokenType;
import com.techsophy.tstokens.exception.InvalidDataException;
import com.techsophy.tstokens.repository.DepartmentRepository;
import com.techsophy.tstokens.repository.OrganizationRepository;
import com.techsophy.tstokens.repository.TokenCategoryRepository;
import com.techsophy.tstokens.repository.TokenTypeRepository;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ValidationUtils {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final TokenTypeRepository tokenTypeRepository;

    @Autowired
    public ValidationUtils(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, TokenCategoryRepository tokenCategoryRepository, TokenTypeRepository tokenTypeRepository) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
        this.tokenTypeRepository = tokenTypeRepository;
    }

    public void validateOrganization(OrganizationCreateRequestPayload requestPayload) {
        logger.info("In validateOrganization()");
        Map<String, String> errors = new HashMap<>();
        //Duplicate
        Optional<Organization> orgOpt = organizationRepository.findByCode(requestPayload.getCode());
        if (orgOpt.isPresent()) {
            errors.put("ORGCODE", "Organization Code already exists");
        }
        orgOpt = organizationRepository.findByName(requestPayload.getName());
        if (orgOpt.isPresent()) {
            errors.put("ORGNAME", "Organization Name already exists");
        }
        for (DepartmentCreateRequestPayload department : requestPayload.getDepartments()) {
            validateDepartment(requestPayload.getCode(), department, errors);
        }
        if (!errors.isEmpty()) {
            throw new InvalidDataException(new JSONObject(errors).toString());
        }
    }

    public void validateDepartment(String orgCode, DepartmentCreateRequestPayload requestPayload, Map<String, String> errors) {
        //Duplicate
        Optional<Department> orgOpt = departmentRepository.findByOrganizationCodeAndCode(orgCode, requestPayload.getCode());
        if (orgOpt.isPresent()) {
            errors.put("DPTCODE", "Department Code already exists");
        }
        orgOpt = departmentRepository.findByOrganizationCodeAndName(orgCode, requestPayload.getName());
        if (orgOpt.isPresent()) {
            errors.put("DPTNAME", "Department Name already exists");
        }
        for (TokenCategoryCreateRequestPayload category : requestPayload.getTokenCategories()) {
            validateTokenCategory(orgCode, requestPayload.getCode(), category, errors);
        }
    }

    public void validateTokenCategory(String orgCode, String deptCode, TokenCategoryCreateRequestPayload requestPayload, Map<String, String> errors) {
        //Duplicate
        Optional<TokenCategory> orgOpt = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(orgCode, deptCode, requestPayload.getCode());
        if (orgOpt.isPresent()) {
            errors.put("TCTCODE", "Token Category Code already exists");
        }
        orgOpt = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndName(orgCode, deptCode, requestPayload.getName());
        if (orgOpt.isPresent()) {
            errors.put("TCTNAME", "Token Category Name already exists");
        }
        for (TokenTypeCreateRequestPayload tokenType : requestPayload.getTokenTypes()) {
            validateTokenType(orgCode, deptCode, requestPayload.getCode(), tokenType, errors);
        }
    }

    public void validateTokenType(String orgCode, String deptCode, String catCode, TokenTypeCreateRequestPayload requestPayload, Map<String, String> errors) {
        //Duplicate
        Optional<TokenType> orgOpt = tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(orgCode, deptCode, catCode, requestPayload.getCode());
        if (orgOpt.isPresent()) {
            errors.put("TTYCODE", "Token Type Code already exists");
        }
        orgOpt = tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndName(orgCode, deptCode, catCode, requestPayload.getName());
        if (orgOpt.isPresent()) {
            errors.put("TTYNAME", "Token Type Name already exists");
        }
    }
}
