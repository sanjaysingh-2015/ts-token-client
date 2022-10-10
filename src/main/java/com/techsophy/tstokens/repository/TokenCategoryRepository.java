package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.TokenCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenCategoryRepository extends MongoRepository<TokenCategory, String> {
    List<TokenCategory> findByOrganizationCodeAndDepartmentCode(String orgCode, String deptCode);
    Optional<TokenCategory> findByOrganizationCodeAndDepartmentCodeAndCode(String orgCode, String deptCode, String code);
    Optional<TokenCategory> findByOrganizationCodeAndDepartmentCodeAndName(String orgCode, String deptCode, String name);

    List<TokenCategory> findByDepartmentCode(String deptCode);
    Optional<TokenCategory> findByCode(String code);
    Optional<TokenCategory> findByDepartmentCodeAndCode(String deptCode, String code);
    Optional<TokenCategory> findByDepartmentCodeAndName(String deptCode, String name);

    Optional<TokenCategory> findByOrganizationCodeAndDepartmentCodeAndCodeAndStatus(String orgCode, String deptCode, String code, String status);
}
