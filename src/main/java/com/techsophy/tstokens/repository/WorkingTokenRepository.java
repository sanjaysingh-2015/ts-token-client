package com.techsophy.tstokens.repository;

import ch.qos.logback.core.subst.Token;
import com.techsophy.tstokens.entity.TokenConfiguration;
import com.techsophy.tstokens.entity.WorkingToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WorkingTokenRepository extends MongoRepository<WorkingToken, String> {
    List<WorkingToken> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndWorkDate(String orgCode, String deptCode, String tokenCat, String tokenTypeCode, Date workDate);
}
