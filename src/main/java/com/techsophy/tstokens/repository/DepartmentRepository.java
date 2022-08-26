package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.Department;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends MongoRepository<Department, String> {
    List<Department> findByOrganizationCode(String orgCode);
    Optional<Department> findByOrganizationCodeAndCode(String orgCode, String code);
    Optional<Department> findByOrganizationCodeAndName(String orgCode, String name);
}
