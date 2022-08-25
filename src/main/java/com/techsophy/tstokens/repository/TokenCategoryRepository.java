package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.TokenCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenCategoryRepository extends MongoRepository<TokenCategory, String> {
    Optional<TokenCategory> findByOrganizationCodeAndDepartmentCodeAndCode(String orgCode, String deptCode, String code);

    Optional<TokenCategory> findByOrganizationCodeAndDepartmentCodeAndName(String orgCode, String deptCode, String name);
}
