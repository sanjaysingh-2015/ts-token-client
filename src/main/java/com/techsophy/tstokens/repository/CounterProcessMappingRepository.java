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

    List<CounterProcessMapping> findByOrganizationCodeAndCounterCode(String orgCode, String counterId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndCounterCode(String orgCode, String deptCode, String counterId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterCode(String orgCode, String deptCode, String catCode, String counterId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCode(String orgCode, String deptCode, String catCode, String typeCode, String counterId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCodeAndProcessStageCode(String orgCode, String deptCode, String catCode, String tokenTypeCode, String counterId, String processStageId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterCodeAndProcessStageCode(String orgCode, String deptCode, String catCode, String counterId, String processStageId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndCounterCodeAndProcessStageCode(String orgCode, String deptCode, String counterId, String processStageId);

    List<CounterProcessMapping> findByOrganizationCodeAndCounterCodeAndProcessStageCode(String orgCode, String counterId, String processStageId);

    List<CounterProcessMapping> findByCounterCode(String counterId);
    List<CounterProcessMapping> findByCounterCodeAndStatus(String counterId, String status);
    List<CounterProcessMapping> findByProcessStageCode(String processStageId);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCodeAndProcessStageCodeAndStatus(String orgCode, String deptCode, String catCode, String tokenTypeCode, String counterId, String processStageId, String status);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterCodeAndProcessStageCodeAndStatus(String orgCode, String deptCode, String catCode, String counterId, String processStageId, String status);

    List<CounterProcessMapping> findByOrganizationCodeAndDepartmentCodeAndCounterCodeAndProcessStageCodeAndStatus(String orgCode, String deptCode, String counterId, String processStageId, String status);

    List<CounterProcessMapping> findByOrganizationCodeAndCounterCodeAndProcessStageCodeAndStatus(String orgCode, String counterId, String processStageId, String status);

}
