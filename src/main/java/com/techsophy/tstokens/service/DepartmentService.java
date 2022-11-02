package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.org.DepartmentCreateRequestPayload;
import com.techsophy.tstokens.dto.org.DepartmentResponsePayload;
import com.techsophy.tstokens.dto.org.DepartmentUpdateRequestPayload;
import com.techsophy.tstokens.dto.org.TokenCategoryUpdateRequestPayload;
import com.techsophy.tstokens.entity.Department;
import com.techsophy.tstokens.entity.Organization;
import com.techsophy.tstokens.entity.TokenCategory;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.DepartmentRepository;
import com.techsophy.tstokens.repository.OrganizationRepository;
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
public class DepartmentService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final ValidationUtils validationUtils;
    private final TokenCategoryService tokenCategoryService;

    private static final String CREATED = "ACTIVE";
    private static final String DELETED = "DELETED";

    @Autowired
    public DepartmentService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, ValidationUtils validationUtils, TokenCategoryService tokenCategoryService) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.validationUtils = validationUtils;
        this.tokenCategoryService = tokenCategoryService;
    }

    public DepartmentResponsePayload getDepartmentDetails(String orgCode, String deptCode) {
        logger.info("In getDepartmentDetails()");
        Optional<Department> departmentOpt = departmentRepository.findByOrganizationCodeAndCode(orgCode, deptCode);
        DepartmentResponsePayload response = null;
        ApplicationMapping<DepartmentResponsePayload, Department> responseMapping = new ApplicationMapping<>();
        if (departmentOpt.isPresent()) {
            response = responseMapping.convert(departmentOpt.get(), DepartmentResponsePayload.class);
        }
        return response;
    }

    public List<DepartmentResponsePayload> getDepartmentList(String orgCode) {
        logger.info("In getDepartmentList()");
        List<Department> departmentList = new ArrayList<>();
        if (!StringUtils.isEmpty(orgCode)) {
            departmentList = departmentRepository.findByOrganizationCode(orgCode);
        }
        List<DepartmentResponsePayload> response = new ArrayList<>();
        ApplicationMapping<DepartmentResponsePayload, Department> responseMapping = new ApplicationMapping<>();
        if(departmentList != null) {
            departmentList.forEach(department ->
                    response.add(responseMapping.convert(department, DepartmentResponsePayload.class))
            );
        }
        return response;
    }

    public DepartmentResponsePayload createDepartment(String orgCode, DepartmentCreateRequestPayload requestPayload) {
        logger.info("In createOrganization()");
        Optional<Organization> organizationOpt = organizationRepository.findByCode(orgCode);
        if (organizationOpt.isEmpty()) {
            throw new ResourceNotFoundException("Organization does not exists with this code");
        }
        Map<String, String> errors = new HashMap<>();
        //validationUtils.validateDepartment(requestPayload, errors);
        ApplicationMapping<Department, DepartmentCreateRequestPayload> mapping = new ApplicationMapping<>();
        Department department = mapping.convert(requestPayload, Department.class);
        department.setOrganizationCode(orgCode);
        return saveDepartment(organizationOpt.get(), department);
    }

    public DepartmentResponsePayload saveDepartment(Organization organization, Department department) {
        logger.info("In saveDepartment()");
        department.setOrganizationCode(organization.getCode());
        department.setCreatedOn(new Date());
        department.setStatus(CREATED);
        department.setCode(SecurityUtils.generateCode(department.getName(), 4));
        departmentRepository.save(department);
        if (department.getTokenCategories() != null) {
            for (TokenCategory tokenCategory : department.getTokenCategories()) {
                tokenCategoryService.saveTokenCategory(department, tokenCategory);
            }
        }
        ApplicationMapping<DepartmentResponsePayload, Department> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(department, DepartmentResponsePayload.class);
    }

    public DepartmentResponsePayload updateDepartment(String orgCode, String deptCode, DepartmentUpdateRequestPayload requestPayload) {
        logger.info("In updateDepartment()");
        Optional<Department> departmentOpt;
        if (!StringUtils.isEmpty(deptCode)) {
            departmentOpt = departmentRepository.findByOrganizationCodeAndCode(orgCode, deptCode);
            if (departmentOpt.isEmpty()) {
                throw new ResourceNotFoundException("Invalid Department to update");
            }
        } else {
            departmentOpt = departmentRepository.findByOrganizationCodeAndCode(orgCode, deptCode);
        }
        Department department = new Department();
        if (departmentOpt.isEmpty()) {
            department.setStatus(CREATED);
            department.setCreatedOn(new Date());
        } else {
            department = departmentOpt.get();
            department.setStatus(requestPayload.getStatus());
            department.setUpdatedOn(new Date());
        }
        department.setName(requestPayload.getName());
        department.setTokenPrefix(requestPayload.getTokenPrefix());
        departmentRepository.save(department);
        if (requestPayload.getTokenCategories() != null) {
            for (TokenCategoryUpdateRequestPayload tokenCategory : requestPayload.getTokenCategories()) {
                tokenCategoryService.updateTokenCategory(orgCode, department.getCode(), tokenCategory);
            }
        }
        ApplicationMapping<DepartmentResponsePayload, Department> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(department, DepartmentResponsePayload.class);
    }

    public String deleteDepartment(String deptId) {
        logger.info("In deleteDepartment()");
        Department department = departmentRepository.findById(deptId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid Department to delete"));
        department.setStatus(DELETED);
        department.setUpdatedOn(new Date());
        departmentRepository.save(department);
        return "Department Deleted successfully";
    }

}
