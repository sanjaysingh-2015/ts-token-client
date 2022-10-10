package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.master.UserCreateRequestPayload;
import com.techsophy.tstokens.dto.master.UserResponsePayload;
import com.techsophy.tstokens.dto.org.DepartmentUpdateRequestPayload;
import com.techsophy.tstokens.dto.org.OrganizationCreateRequestPayload;
import com.techsophy.tstokens.dto.org.OrganizationResponsePayload;
import com.techsophy.tstokens.dto.org.OrganizationUpdateRequestPayload;
import com.techsophy.tstokens.entity.Department;
import com.techsophy.tstokens.entity.Organization;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.OrganizationRepository;
import com.techsophy.tstokens.utils.ApplicationMapping;
import com.techsophy.tstokens.utils.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final OrganizationRepository organizationRepository;
    private final UserConfigService userConfigService;
    private final DepartmentService departmentService;
    private final PasswordEncoder passwordEncoder;

    private static final String CREATED = "ACTIVE";
    private static final String DELETED = "DELETED";
    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository, UserConfigService userConfigService, DepartmentService departmentService,PasswordEncoder passwordEncoder) {
        this.organizationRepository = organizationRepository;
        this.userConfigService = userConfigService;
        this.departmentService = departmentService;
        this.passwordEncoder = passwordEncoder;
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
        UserResponsePayload userResponsePayload = createUserForOrganization(organization);
        ApplicationMapping<OrganizationResponsePayload, Organization> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(organization, OrganizationResponsePayload.class);
    }

    private UserResponsePayload createUserForOrganization(Organization organization) {
        UserCreateRequestPayload userRequest = new UserCreateRequestPayload();
        userRequest.setEmail(organization.getEmail());
        userRequest.setRole("CLIENT");
        userRequest.setFirstName(organization.getFirstName());
        userRequest.setLastName(organization.getLastName());
        userRequest.setPassword(passwordEncoder.encode("ChangeIt@Now"));
        return userConfigService.createUser(userRequest);
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