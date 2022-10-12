package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.CounterDeviceMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterDeviceMappingRepository extends MongoRepository<CounterDeviceMapping, String> {
    List<CounterDeviceMapping> findByCounterId(String counterId);
    List<CounterDeviceMapping> findByDeviceId(String deviceId);
}
