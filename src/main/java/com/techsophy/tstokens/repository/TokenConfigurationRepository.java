package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.TokenConfiguration;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface TokenConfigurationRepository extends MongoRepository<TokenConfiguration, String> {
    Optional<TokenConfiguration> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndWorkDate(String orgCode, String deptCode, String tokenCat, String tokenTypeCode, Date workDate);

    Optional<TokenConfiguration> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndWorkDate(String orgCode, String deptCode, String tokenCat, Date workDate);

    Optional<TokenConfiguration> findByOrganizationCodeAndDepartmentCodeAndWorkDate(String orgCode, String deptCode, Date workDate);

    Optional<TokenConfiguration> findByOrganizationCodeAndWorkDate(String orgCode, Date workDate);
}
