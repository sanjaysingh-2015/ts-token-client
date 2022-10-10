package com.techsophy.tstokens.utils;

import com.techsophy.tstokens.repository.DepartmentRepository;
import com.techsophy.tstokens.repository.OrganizationRepository;
import com.techsophy.tstokens.repository.TokenCategoryRepository;
import com.techsophy.tstokens.repository.TokenTypeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
//
//    public void validateOrganization(OrganizationCreateRequestPayload requestPayload) {
//        logger.info("In validateOrganization()");
//        Map<String, String> errors = new HashMap<>();
//        //Duplicate
//        Optional<Organization> orgOpt = organizationRepository.findByName(requestPayload.getName());
//        if (orgOpt.isPresent()) {
//            errors.put("ORGNAME", "Organization Name already exists");
//        }
//        if(requestPayload.getDepartments() != null) {
//            for (DepartmentCreateRequestPayload department : requestPayload.getDepartments()) {
//                validateDepartment(department, errors);
//            }
//            if (!errors.isEmpty()) {
//                throw new InvalidDataException(new JSONObject(errors).toString());
//            }
//        }
//    }
//
//    public void validateDepartment(DepartmentCreateRequestPayload requestPayload, Map<String, String> errors) {
//        //Duplicate
//        Optional<Department> orgOpt = departmentRepository.findByName(requestPayload.getName());
//        if (orgOpt.isPresent()) {
//            errors.put("DPTNAME", "Department Name already exists");
//        }
//        if(requestPayload.getTokenCategories() != null) {
//            for (TokenCategoryCreateRequestPayload category : requestPayload.getTokenCategories()) {
//                validateTokenCategory(category, errors);
//            }
//        }
//    }
//
//    public void validateTokenCategory(TokenCategoryCreateRequestPayload requestPayload, Map<String, String> errors) {
//        //Duplicate
//        Optional<TokenCategory> orgOpt = tokenCategoryRepository.findByDepartmentCodeAndCode(deptCode, requestPayload.getCode());
//        if (orgOpt.isPresent()) {
//            errors.put("TCTCODE", "Token Category Code already exists");
//        }
//        orgOpt = tokenCategoryRepository.findByDepartmentCodeAndName(deptCode, requestPayload.getName());
//        if (orgOpt.isPresent()) {
//            errors.put("TCTNAME", "Token Category Name already exists");
//        }
//        if(requestPayload.getTokenTypes() != null) {
//            for (TokenTypeCreateRequestPayload tokenType : requestPayload.getTokenTypes()) {
//                validateTokenType(requestPayload.getCode(), tokenType, errors);
//            }
//        }
//    }
//
//    public void validateTokenType(String catCode, TokenTypeCreateRequestPayload requestPayload, Map<String, String> errors) {
//        //Duplicate
//        Optional<TokenType> orgOpt = tokenTypeRepository.findByTokenCategoryCodeAndCode(catCode, requestPayload.getCode());
//        if (orgOpt.isPresent()) {
//            errors.put("TTYCODE", "Token Type Code already exists");
//        }
//        orgOpt = tokenTypeRepository.findByTokenCategoryCodeAndName(catCode, requestPayload.getName());
//        if (orgOpt.isPresent()) {
//            errors.put("TTYNAME", "Token Type Name already exists");
//        }
//    }
}
