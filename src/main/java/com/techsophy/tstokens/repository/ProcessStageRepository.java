package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.ProcessStage;
import com.techsophy.tstokens.entity.WorkingToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

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
}
