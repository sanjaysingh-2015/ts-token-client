package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.TokenType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenTypeRepository extends MongoRepository<TokenType, String> {
    List<TokenType> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(String orgCode, String deptCode, String tokenCat);
    Optional<TokenType> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(String orgCode, String deptCode, String tokenCat, String code);
    Optional<TokenType> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndName(String orgCode, String deptCode, String tokenCat, String name);

    List<TokenType> findByTokenCategoryCode(String tokenCat);
    Optional<TokenType> findByTokenCategoryCodeAndCode(String tokenCat, String code);
    Optional<TokenType> findByTokenCategoryCodeAndName(String tokenCat, String name);
    Optional<TokenType> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCodeAndStatus(String orgCode, String deptCode, String tokenCat, String code, String status);
}
