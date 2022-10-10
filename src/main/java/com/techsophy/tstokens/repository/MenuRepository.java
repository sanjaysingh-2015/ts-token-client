package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.Menu;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends MongoRepository<Menu, String> {
    List<Menu> findByStatus(String status);
}
