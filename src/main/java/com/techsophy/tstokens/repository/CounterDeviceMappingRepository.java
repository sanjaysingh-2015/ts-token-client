package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.CounterDeviceMapping;
import com.techsophy.tstokens.entity.CounterDeviceMapping;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterDeviceMappingRepository extends MongoRepository<CounterDeviceMapping, String> {
    List<CounterDeviceMapping> findByOrganizationCode(String orgCode);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCode(String orgCode, String deptCode);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(String orgCode, String deptCode, String catCode);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(String orgCode, String deptCode, String catCode, String typeCode);

    List<CounterDeviceMapping> findByOrganizationCodeAndCounterCode(String orgCode, String counterId);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCodeAndCounterCode(String orgCode, String deptCode, String counterId);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterCode(String orgCode, String deptCode, String catCode, String counterId);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCode(String orgCode, String deptCode, String catCode, String typeCode, String counterId);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCodeAndDeviceUid(String orgCode, String deptCode, String catCode, String tokenTypeCode, String counterId, String processStageId);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterCodeAndDeviceUid(String orgCode, String deptCode, String catCode, String counterId, String processStageId);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCodeAndCounterCodeAndDeviceUid(String orgCode, String deptCode, String counterId, String processStageId);

    List<CounterDeviceMapping> findByOrganizationCodeAndCounterCodeAndDeviceUid(String orgCode, String counterId, String processStageId);

    List<CounterDeviceMapping> findByCounterCode(String counterId);
    List<CounterDeviceMapping> findByCounterCodeAndStatus(String counterId, String status);
    List<CounterDeviceMapping> findByDeviceUid(String processStageId);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCounterCodeAndDeviceUidAndStatus(String orgCode, String deptCode, String catCode, String tokenTypeCode, String counterId, String processStageId, String status);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCounterCodeAndDeviceUidAndStatus(String orgCode, String deptCode, String catCode, String counterId, String processStageId, String status);

    List<CounterDeviceMapping> findByOrganizationCodeAndDepartmentCodeAndCounterCodeAndDeviceUidAndStatus(String orgCode, String deptCode, String counterId, String processStageId, String status);

    List<CounterDeviceMapping> findByOrganizationCodeAndCounterCodeAndDeviceUidAndStatus(String orgCode, String counterId, String processStageId, String status);

}
