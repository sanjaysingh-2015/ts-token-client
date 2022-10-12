package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.ProcessStatus;
import com.techsophy.tstokens.entity.ProcessStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessStatusRepository extends MongoRepository<ProcessStatus, String> {
    List<ProcessStatus> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(String orgCode, String deptCode, String tokenCat, String tokenTypeCode);
    List<ProcessStatus> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(String orgCode, String deptCode, String tokenCat);
    List<ProcessStatus> findByOrganizationCodeAndDepartmentCode(String orgCode, String deptCode);
    List<ProcessStatus> findByOrganizationCode(String orgCode);

    List<ProcessStatus> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndStatus(String orgCode, String deptCode, String tokenCat, String tokenTypeCode, String status);
    List<ProcessStatus> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndStatus(String orgCode, String deptCode, String tokenCat, String status);
    List<ProcessStatus> findByOrganizationCodeAndDepartmentCodeAndStatus(String orgCode, String deptCode, String status);
    List<ProcessStatus> findByOrganizationCodeAndStatus(String orgCode, String status);

    Optional<ProcessStatus> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(String orgCode, String deptCode, String tokenCat, String tokenTypeCode, String code);
    Optional<ProcessStatus> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(String orgCode, String deptCode, String tokenCat, String code);
    Optional<ProcessStatus> findByOrganizationCodeAndDepartmentCodeAndCode(String orgCode, String deptCode, String code);
    Optional<ProcessStatus> findByOrganizationCodeAndCode(String orgCode, String code);
    Optional<ProcessStatus> findByCode(String code);
}
