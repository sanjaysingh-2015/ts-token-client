package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.TokenType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenTypeRepository extends MongoRepository<TokenType, String> {
    Optional<TokenType> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(String orgCode, String deptCode, String tokenCat, String code);

    Optional<TokenType> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndName(String orgCode, String deptCode, String tokenCat, String name);
}
