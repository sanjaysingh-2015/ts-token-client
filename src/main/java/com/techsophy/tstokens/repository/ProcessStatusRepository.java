package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.ProcessStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcessStatusRepository extends MongoRepository<ProcessStatus, String> {
    List<ProcessStatus> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(String orgCode, String deptCode, String tokenCat, String tokenTypeCode);
    List<ProcessStatus> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(String orgCode, String deptCode, String tokenCat);
    List<ProcessStatus> findByOrganizationCodeAndDepartmentCode(String orgCode, String deptCode);
    List<ProcessStatus> findByOrganizationCode(String orgCode);
}
