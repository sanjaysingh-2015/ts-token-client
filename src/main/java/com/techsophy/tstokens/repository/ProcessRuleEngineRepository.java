package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.ProcessRuleEngine;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessRuleEngineRepository extends MongoRepository<ProcessRuleEngine, String> {
    List<ProcessRuleEngine> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(String orgCode, String deptCode, String tokenCat, String tokenTypeCode);

    List<ProcessRuleEngine> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(String orgCode, String deptCode, String tokenCat);

    List<ProcessRuleEngine> findByOrganizationCodeAndDepartmentCode(String orgCode, String deptCode);

    List<ProcessRuleEngine> findByOrganizationCode(String orgCode);

    Optional<ProcessRuleEngine> findByCurrentStageCodeAndCurrentStatusCode(String stageCode, String statusCode);

    List<ProcessRuleEngine> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndStatus(String orgCode, String deptCode, String tokenCat, String tokenTypeCode, String status);

    List<ProcessRuleEngine> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndStatus(String orgCode, String deptCode, String tokenCat, String status);

    List<ProcessRuleEngine> findByOrganizationCodeAndDepartmentCodeAndStatus(String orgCode, String deptCode, String status);

    List<ProcessRuleEngine> findByOrganizationCodeAndStatus(String orgCode, String status);

    Optional<ProcessRuleEngine> findByCurrentStageCodeAndCurrentStatusCodeAndStatus(String stageCode, String statusCode, String status);

}
