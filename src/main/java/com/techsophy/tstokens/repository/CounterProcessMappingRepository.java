package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.Counter;
import com.techsophy.tstokens.entity.CounterProcessMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterProcessMappingRepository extends MongoRepository<CounterProcessMapping, String> {
    List<CounterProcessMapping> findByCounterId(String counterId);
    List<CounterProcessMapping> findByProcessStageId(String processStageId);
}
