package com.techsophy.tstokens.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document("cf_counter_device_mapping")
public class CounterDeviceMapping {
    @Id
    private String id;
    @NotNull
    @Size(max = 10)
    private String organizationCode;
    @Size(max = 10)
    private String departmentCode;
    @Size(max = 10)
    private String tokenCategoryCode;
    @Size(max = 10)
    private String tokenTypeCode;
    @NotNull
    private String counterCode;
    @NotNull
    private String deviceUid;
    private String status;

    public CounterDeviceMapping(String organizationCode, String departmentCode, String tokenCategoryCode, String tokenTypeCode, String counterCode, String deviceUid, String status) {
        this.organizationCode = organizationCode;
        this.departmentCode = departmentCode;
        this.tokenCategoryCode = tokenCategoryCode;
        this.tokenTypeCode = tokenTypeCode;
        this.counterCode = counterCode;
        this.deviceUid = deviceUid;
        this.status = status;
    }
}
