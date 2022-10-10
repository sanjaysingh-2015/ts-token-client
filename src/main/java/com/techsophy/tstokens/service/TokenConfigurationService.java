package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.api.TokenInitializeApiRequestPayload;
import com.techsophy.tstokens.entity.*;
import com.techsophy.tstokens.exception.InvalidOperationException;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
public class TokenConfigurationService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final TokenConfigurationRepository tokenConfigurationRepository;

    private static final String ACTIVE = "ACTIVE";
    private static final String CONFIG_STATUS_INIT = "INITIALIZED";
    private static final String CONFIG_STATUS_CLOSED = "CLOSED";

    @Autowired
    public TokenConfigurationService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, TokenCategoryRepository tokenCategoryRepository, TokenTypeRepository tokenTypeRepository, TokenConfigurationRepository tokenConfigurationRepository) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
        this.tokenTypeRepository = tokenTypeRepository;
        this.tokenConfigurationRepository = tokenConfigurationRepository;
    }

    public String initializeTokenForDate(TokenInitializeApiRequestPayload request) {
        Optional<TokenConfiguration> tokenConfigurationOpt = Optional.empty();
        Organization organization = organizationRepository.findByCodeAndStatus(request.getOrganizationCode(),ACTIVE).orElseThrow(
                () -> new ResourceNotFoundException("Invalid Organization"));
        Optional<Department> department = departmentRepository.findByOrganizationCodeAndCodeAndStatus(request.getOrganizationCode(), request.getDepartmentCode(), ACTIVE);
        if(department.isPresent()) {
            Optional<TokenCategory> category = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCodeAndStatus(request.getOrganizationCode(), request.getDepartmentCode(), request.getCategoryCode(), ACTIVE);
            if(category.isPresent()) {
                Optional<TokenType> tokenType = tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCodeAndStatus(request.getOrganizationCode(), request.getDepartmentCode(), request.getCategoryCode(), request.getTokenTypeCode(), ACTIVE);
                if(tokenType.isPresent()) {
                    tokenConfigurationOpt = tokenConfigurationRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndWorkDate(request.getOrganizationCode(), request.getDepartmentCode(), request.getCategoryCode(), request.getTokenTypeCode(), request.getWorkDate());
                } else {
                    tokenConfigurationOpt = tokenConfigurationRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndWorkDate(request.getOrganizationCode(), request.getDepartmentCode(), request.getCategoryCode(), request.getWorkDate());
                }
            } else {
                tokenConfigurationOpt = tokenConfigurationRepository.findByOrganizationCodeAndDepartmentCodeAndWorkDate(request.getOrganizationCode(), request.getDepartmentCode(), request.getWorkDate());
            }
        } else {
            tokenConfigurationOpt = tokenConfigurationRepository.findByOrganizationCodeAndWorkDate(request.getOrganizationCode(), request.getWorkDate());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(tokenConfigurationOpt.isPresent()) {
            throw new InvalidOperationException("TST-901: Token Initialization Already Done for Date: "+ simpleDateFormat.format(request.getWorkDate()));
        } else {
            TokenConfiguration tokenConfiguration = new TokenConfiguration();
            tokenConfiguration.setOrganizationCode(request.getOrganizationCode());
            tokenConfiguration.setDepartmentCode(request.getDepartmentCode());
            tokenConfiguration.setTokenCategoryCode(request.getCategoryCode());
            tokenConfiguration.setTokenTypeCode(request.getTokenTypeCode());
            tokenConfiguration.setCurrentTokenNo(1);
            tokenConfiguration.setWorkDate(request.getWorkDate());
            tokenConfiguration.setCreatedOn(new Date());
            tokenConfiguration.setUserName(request.getUserName());
            tokenConfiguration.setStatus(CONFIG_STATUS_INIT);
            tokenConfigurationRepository.save(tokenConfiguration);
            return "Token Initialization Done for Date: "+ simpleDateFormat.format(request.getWorkDate());
        }
    }

    public String closeTokenForDate(TokenInitializeApiRequestPayload request) {
        Optional<TokenConfiguration> tokenConfigurationOpt = Optional.empty();
        Organization organization = organizationRepository.findByCodeAndStatus(request.getOrganizationCode(),ACTIVE).orElseThrow(
                () -> new ResourceNotFoundException("Invalid Organization"));
        Optional<Department> department = departmentRepository.findByOrganizationCodeAndCodeAndStatus(request.getOrganizationCode(), request.getDepartmentCode(), ACTIVE);
        if(department.isPresent()) {
            Optional<TokenCategory> category = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCodeAndStatus(request.getOrganizationCode(), request.getDepartmentCode(), request.getCategoryCode(), ACTIVE);
            if(category.isPresent()) {
                Optional<TokenType> tokenType = tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCodeAndStatus(request.getOrganizationCode(), request.getDepartmentCode(), request.getCategoryCode(), request.getTokenTypeCode(), ACTIVE);
                if(tokenType.isPresent()) {
                    tokenConfigurationOpt = tokenConfigurationRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndWorkDate(request.getOrganizationCode(), request.getDepartmentCode(), request.getCategoryCode(), request.getTokenTypeCode(), request.getWorkDate());
                } else {
                    tokenConfigurationOpt = tokenConfigurationRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndWorkDate(request.getOrganizationCode(), request.getDepartmentCode(), request.getCategoryCode(), request.getWorkDate());
                }
            } else {
                tokenConfigurationOpt = tokenConfigurationRepository.findByOrganizationCodeAndDepartmentCodeAndWorkDate(request.getOrganizationCode(), request.getDepartmentCode(), request.getWorkDate());
            }
        } else {
            tokenConfigurationOpt = tokenConfigurationRepository.findByOrganizationCodeAndWorkDate(request.getOrganizationCode(), request.getWorkDate());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        if(tokenConfigurationOpt.isPresent()) {
            if(tokenConfigurationOpt.get().getStatus().equalsIgnoreCase(CONFIG_STATUS_CLOSED)) {
                throw new InvalidOperationException("TST-903: Token Service Already Closed for Date: "+ simpleDateFormat.format(request.getWorkDate()));
            }
            tokenConfigurationOpt.get().setStatus(CONFIG_STATUS_CLOSED);
            tokenConfigurationRepository.save(tokenConfigurationOpt.get());
            return "Token Service successfully closed for Date: "+ simpleDateFormat.format(request.getWorkDate());
        } else {
            throw new InvalidOperationException("TST-902: Token Yet not Initialization for Date: "+ simpleDateFormat.format(request.getWorkDate()));
        }
    }
}
