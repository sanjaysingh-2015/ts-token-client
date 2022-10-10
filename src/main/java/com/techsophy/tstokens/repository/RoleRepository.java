package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.EnumRole;
import com.techsophy.tstokens.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(EnumRole name);
}
