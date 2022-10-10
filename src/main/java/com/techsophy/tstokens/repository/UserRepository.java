package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String userName);
    Page<User> findByEmailContains(String email, Pageable pageable);
    Page<User> findAllByEmail(String email, Pageable pageable);
    Page<User> findAllByEmailContainsAndEmail(String email, String auth, Pageable pageable);

    Boolean existsByEmail(String email);
}
