package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.org.DepartmentUpdateRequestPayload;
import com.techsophy.tstokens.dto.org.OrganizationCreateRequestPayload;
import com.techsophy.tstokens.dto.org.OrganizationResponsePayload;
import com.techsophy.tstokens.dto.org.OrganizationUpdateRequestPayload;
import com.techsophy.tstokens.entity.Department;
import com.techsophy.tstokens.entity.Organization;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.DepartmentRepository;
import com.techsophy.tstokens.repository.OrganizationRepository;
import com.techsophy.tstokens.repository.TokenCategoryRepository;
import com.techsophy.tstokens.repository.TokenTypeRepository;
import com.techsophy.tstokens.utils.ApplicationMapping;
import com.techsophy.tstokens.utils.SecurityUtils;
import com.techsophy.tstokens.utils.ValidationUtils;
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
    private final DepartmentService departmentService;

    private static final String CREATED = "ACTIVE";
    private static final String DELETED = "DELETED";
    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, TokenCategoryRepository tokenCategoryRepository, TokenTypeRepository tokenTypeRepository, ValidationUtils validationUtils, DepartmentService departmentService) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
        this.tokenTypeRepository = tokenTypeRepository;
        this.validationUtils = validationUtils;
        this.departmentService = departmentService;
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
        ApplicationMapping<Organization, OrganizationCreateRequestPayload> mapping = new ApplicationMapping<>();
        Organization organization = mapping.convert(requestPayload, Organization.class);
        return saveOrganization(organization);
    }
    public OrganizationResponsePayload saveOrganization(Organization organization) {
        logger.info("In saveOrganization()");
        organization.setCreatedOn(new Date());
        organization.setStatus(CREATED);
        organization.setAuthCode(SecurityUtils.generateAuthCode());
        organization.setCode(SecurityUtils.generateCode(organization.getName(), 6));
        if (organization.getDepartments() != null) {
            for (Department department : organization.getDepartments()) {
                departmentService.saveDepartment(organization, department);
            }
        }
        organization = organizationRepository.save(organization);
        ApplicationMapping<OrganizationResponsePayload, Organization> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(organization, OrganizationResponsePayload.class);
    }
    public OrganizationResponsePayload updateOrganization(String id, OrganizationUpdateRequestPayload requestPayload) {
        logger.info("In updateOrganization()");
        Optional<Organization> organizationOpt = organizationRepository.findById(id);
        if (organizationOpt.isEmpty()) {
            throw new ResourceNotFoundException("Invalid Organization to update");
        }
        Organization organization = organizationOpt.get();
        organization.setName(requestPayload.getName());
        organization.setEmail(requestPayload.getEmail());
        organization.setPhoneNo(requestPayload.getPhoneNo());
        organization.setFirstName(requestPayload.getFirstName());
        organization.setMiddleName(requestPayload.getMiddleName());
        organization.setLastName(requestPayload.getLastName());
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
                departmentService.updateDepartment(organization.getCode(), "", department);
            }
        }
        ApplicationMapping<OrganizationResponsePayload, Organization> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(organization, OrganizationResponsePayload.class);
    }

    public String regenerateAuthCode(String orgCode) {
        logger.info("In updateOrganization()");
        Optional<Organization> organizationOpt = organizationRepository.findByCode(orgCode);
        if (organizationOpt.isEmpty()) {
            throw new ResourceNotFoundException("Invalid Organization to update");
        }
        organizationOpt.get().setAuthCode(SecurityUtils.generateAuthCode());
        organizationRepository.save(organizationOpt.get());
        return "Success";
    }

    public String deleteOrganization(String orgId) {
        logger.info("In deleteDepartment()");
        Organization organization = organizationRepository.findById(orgId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid Organization to delete"));
        organization.setStatus(DELETED);
        organization.setUpdatedOn(new Date());
        organizationRepository.save(organization);
        return "Department Deleted successfully";
    }
}