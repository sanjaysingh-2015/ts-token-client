package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.ProcessStage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessStageRepository extends MongoRepository<ProcessStage, String> {
    List<ProcessStage> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(String orgCode, String deptCode, String tokenCat, String tokenTypeCode);
    List<ProcessStage> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(String orgCode, String deptCode, String tokenCat);
    List<ProcessStage> findByOrganizationCodeAndDepartmentCode(String orgCode, String deptCode);
    List<ProcessStage> findByOrganizationCode(String orgCode);

    List<ProcessStage> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndStatus(String orgCode, String deptCode, String tokenCat, String tokenTypeCode, String status);
    List<ProcessStage> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndStatus(String orgCode, String deptCode, String tokenCat, String status);
    List<ProcessStage> findByOrganizationCodeAndDepartmentCodeAndStatus(String orgCode, String deptCode, String status);
    List<ProcessStage> findByOrganizationCodeAndStatus(String orgCode, String status);

    Optional<ProcessStage> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(String orgCode, String deptCode, String tokenCat, String tokenTypeCode, String code);
    Optional<ProcessStage> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(String orgCode, String deptCode, String tokenCat, String code);
    Optional<ProcessStage> findByOrganizationCodeAndDepartmentCodeAndCode(String orgCode, String deptCode, String code);
    Optional<ProcessStage> findByOrganizationCodeAndCode(String orgCode, String code);
    Optional<ProcessStage> findByCode(String code);
}
