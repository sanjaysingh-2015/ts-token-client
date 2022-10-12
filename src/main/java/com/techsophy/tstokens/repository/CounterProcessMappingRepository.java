package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.CounterProcessMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterProcessMappingRepository extends MongoRepository<CounterProcessMapping, String> {
    List<CounterProcessMapping> findByOrganizationCode(String orgCode);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCode(String orgCode, String deptCode);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(String orgCode, String deptCode, String catCode);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(String orgCode, String deptCode, String catCode, String typeCode);

    List<CounterProcessMapping> findByOrganizationCodeAndCounterId(String orgCode, String counterId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndCounterId(String orgCode, String deptCode, String counterId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterId(String orgCode, String deptCode, String catCode, String counterId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterId(String orgCode, String deptCode, String catCode, String typeCode, String counterId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterIdAndProcessStageId(String orgCode, String deptCode, String catCode, String tokenTypeCode, String counterId, String processStageId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterIdAndProcessStageId(String orgCode, String deptCode, String catCode, String counterId, String processStageId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndCounterIdAndProcessStageId(String orgCode, String deptCode, String counterId, String processStageId);

    List<CounterProcessMapping> findByOrganizationCodeAndCounterIdAndProcessStageId(String orgCode, String counterId, String processStageId);

    List<CounterProcessMapping> findByCounterId(String counterId);

    List<CounterProcessMapping> findByProcessStageId(String processStageId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterIdAndProcessStageIdAndStatus(String orgCode, String deptCode, String catCode, String tokenTypeCode, String counterId, String processStageId, String status);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterIdAndProcessStageIdAndStatus(String orgCode, String deptCode, String catCode, String counterId, String processStageId, String status);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndCounterIdAndProcessStageIdAndStatus(String orgCode, String deptCode, String counterId, String processStageId, String status);

    List<CounterProcessMapping> findByOrganizationCodeAndCounterIdAndProcessStageIdAndStatus(String orgCode, String counterId, String processStageId, String status);

}
