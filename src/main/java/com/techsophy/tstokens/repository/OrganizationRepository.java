package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.Organization;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends MongoRepository<Organization, String> {
    Optional<Organization> findByCode(String code);
    Optional<Organization> findByName(String name);
    Optional<Organization> findByCodeAndStatus(String code, String status);
    Optional<Organization> findByAuthCode(String authCode);
}
