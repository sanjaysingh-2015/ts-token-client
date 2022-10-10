package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.api.TokenApiRequestPayload;
import com.techsophy.tstokens.dto.api.TokenApiResponsePayload;
import com.techsophy.tstokens.entity.*;
import com.techsophy.tstokens.repository.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class WorkingTokenService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final TokenTypeRepository tokenTypeRepository;
    private final MongoTemplate mongoTemplate;
    private final WorkingTokenRepository workingTokenRepository;

    private static final String ACTIVE = "ACTIVE";
    private static final String TOKEN_OPEN = "OPEN";
    private static final String TOKEN_ASSIGNED = "ASSIGNED";
    private static final String TOKEN_HOLD = "HOLD";
    private static final String TOKEN_ESCALATED = "ESCALATED";
    private static final String TOKEN_CANCELLED = "CANCELLED";
    private static final String TOKEN_COMPLETED = "COMPLETED";

    @Autowired
    public WorkingTokenService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, TokenCategoryRepository tokenCategoryRepository, TokenTypeRepository tokenTypeRepository, MongoTemplate mongoTemplate, WorkingTokenRepository workingTokenRepository) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
        this.tokenTypeRepository = tokenTypeRepository;
        this.mongoTemplate = mongoTemplate;
        this.workingTokenRepository = workingTokenRepository;
    }

    public TokenApiResponsePayload generateToken(TokenApiRequestPayload requestPayload) {
        return saveAndGenerateToken(requestPayload);
    }

    private TokenApiResponsePayload saveAndGenerateToken(TokenApiRequestPayload request) {
        WorkingToken token = new WorkingToken();
        token.setOrganizationCode(request.getOrganizationCode());
        token.setDepartmentCode(request.getDepartmentCode());
        token.setTokenCategoryCode(request.getTokenCategoryCode());
        token.setTokenTypeCode(request.getTokenTypeCode());
        token.setWorkDate(request.getWorkDate());
        token.setTokenEntity(request.getTokenEntity());
        token.setTokenEntityValue(request.getTokenEntityValue());
        token.setUserName(request.getUserName());
        token.setCounterNo(request.getCounterNo());
        token.setTokenNo(getNextTokenNo(token));
        token.setProcessStageCode("A");
        token.setProcessStatusCode(TOKEN_OPEN);
        token.setCreatedOn(new Date());
        workingTokenRepository.save(token);
        return convert(token);
    }

    private synchronized String getNextTokenNo(WorkingToken token) {
        String prefix = "";
        Query query = new Query();
        if(!StringUtils.isEmpty(token.getOrganizationCode())) {
            Optional<Organization> organization = organizationRepository.findByCodeAndStatus(token.getOrganizationCode(), ACTIVE);
            if(organization.isPresent()) {
                prefix = organization.get().getTokenPrefix();
                if (!StringUtils.isEmpty(token.getDepartmentCode())) {
                    Optional<Department> department = departmentRepository.findByOrganizationCodeAndCodeAndStatus(organization.get().getCode(), token.getDepartmentCode(), ACTIVE);
                    if(department.isPresent()) {
                        prefix = department.get().getTokenPrefix();
                        if (!StringUtils.isEmpty(token.getTokenCategoryCode())) {
                            Optional<TokenCategory> category = tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCodeAndStatus(organization.get().getCode(), department.get().getCode(), token.getTokenCategoryCode(), ACTIVE);
                            if(category.isPresent()) {
                                prefix = category.get().getTokenPrefix();
                                if (!StringUtils.isEmpty(token.getTokenTypeCode())) {
                                    Optional<TokenType> tokenType = tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCodeAndStatus(organization.get().getCode(), department.get().getCode(), category.get().getCode(), token.getTokenTypeCode(), ACTIVE);
                                    if(tokenType.isPresent()) {
                                        prefix = tokenType.get().getTokenPrefix();
                                        query.addCriteria(Criteria.where("organizationCode").is(token.getOrganizationCode())
                                                .and("departmentCode").is(token.getDepartmentCode())
                                                .and("tokenCategoryCode").is(token.getTokenCategoryCode())
                                                .and("tokenTypeCode").is(token.getTokenTypeCode())
                                                .and("workDate").is(token.getWorkDate()));
                                    }
                                } else {
                                    query.addCriteria(Criteria.where("organizationCode").is(token.getOrganizationCode())
                                            .and("departmentCode").is(token.getDepartmentCode())
                                            .and("tokenCategoryCode").is(token.getTokenCategoryCode())
                                            .and("workDate").is(token.getWorkDate()));
                                }
                            }
                        } else {
                            query.addCriteria(Criteria.where("organizationCode").is(token.getOrganizationCode())
                                    .and("departmentCode").is(token.getDepartmentCode())
                                    .and("workDate").is(token.getWorkDate()));
                        }
                    }
                } else {
                    query.addCriteria(Criteria.where("organizationCode").is(token.getOrganizationCode())
                            .and("workDate").is(token.getWorkDate()));
                }
            }
        }

        Update update = new Update();
        update.inc("currentTokenNo", 1);
        FindAndModifyOptions findAndModifyOptions = FindAndModifyOptions.options().returnNew(true);
        TokenConfiguration tokenConfiguration = mongoTemplate.findAndModify(query, update, findAndModifyOptions, TokenConfiguration.class);
        return prefix + String.format("%05d", tokenConfiguration.getCurrentTokenNo());
    }

    private TokenApiResponsePayload convert(WorkingToken token) {
        TokenApiResponsePayload response = new TokenApiResponsePayload();
        response.setOrganizationCode(token.getOrganizationCode());
        response.setDepartmentCode(token.getDepartmentCode());
        response.setTokenCategoryCode(token.getTokenCategoryCode());
        response.setTokenTypeCode(token.getTokenTypeCode());
        response.setWorkDate(token.getWorkDate());
        response.setTokenEntity(token.getTokenEntity());
        response.setTokenEntityValue(token.getTokenEntityValue());
        response.setUserName(token.getUserName());
        response.setCounterNo(token.getCounterNo());
        response.setTokenNo(token.getTokenNo());
        response.setProcessStageCode("A");
        response.setProcessStatusCode(TOKEN_OPEN);
        return response;
    }
}
