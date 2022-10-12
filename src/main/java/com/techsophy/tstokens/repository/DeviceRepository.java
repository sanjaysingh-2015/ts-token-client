package com.techsophy.tstokens.repository;

import com.techsophy.tstokens.entity.Counter;
import com.techsophy.tstokens.entity.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {
    List<Device> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(String orgCode, String deptCode, String tokenCat, String tokenTypeCode);
    List<Device> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(String orgCode, String deptCode, String tokenCat);
    List<Device> findByOrganizationCodeAndDepartmentCode(String orgCode, String deptCode);
    List<Device> findByOrganizationCode(String orgCode);

    List<Device> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndStatus(String orgCode, String deptCode, String tokenCat, String tokenTypeCode, String status);
    List<Device> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndStatus(String orgCode, String deptCode, String tokenCat, String status);
    List<Device> findByOrganizationCodeAndDepartmentCodeAndStatus(String orgCode, String deptCode, String status);
    List<Device> findByOrganizationCodeAndStatus(String orgCode, String status);

    Optional<Device> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndDeviceUid(String orgCode, String deptCode, String tokenCat, String tokenTypeCode, String counterNo);
    Optional<Device> findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndDeviceUid(String orgCode, String deptCode, String tokenCat, String counterNo);
    Optional<Device> findByOrganizationCodeAndDepartmentCodeAndDeviceUid(String orgCode, String deptCode, String counterNo);
    Optional<Device> findByOrganizationCodeAndDeviceUid(String orgCode, String counterNo);
    Optional<Device> findByDeviceUid(String counterNo);
}
