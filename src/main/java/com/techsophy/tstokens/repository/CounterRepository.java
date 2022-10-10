package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterRepository extends MongoRepository<Counter, String> {
    List<Counter> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(String orgCode, String deptCode, String tokenCat, String tokenTypeCode);
    List<Counter> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(String orgCode, String deptCode, String tokenCat);
    List<Counter> findByOrganizationCodeAndDepartmentCode(String orgCode, String deptCode);
    List<Counter> findByOrganizationCode(String orgCode);

    List<Counter> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndStatus(String orgCode, String deptCode, String tokenCat, String tokenTypeCode, String status);
    List<Counter> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndStatus(String orgCode, String deptCode, String tokenCat, String status);
    List<Counter> findByOrganizationCodeAndDepartmentCodeAndStatus(String orgCode, String deptCode, String status);
    List<Counter> findByOrganizationCodeAndStatus(String orgCode, String status);
}
