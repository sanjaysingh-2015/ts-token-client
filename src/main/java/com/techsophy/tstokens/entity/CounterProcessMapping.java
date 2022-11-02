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
@Document("cf_counter_process_stage_mapping")
public class CounterProcessMapping {
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
    @Size(max = 10)
    private String counterCode;
    @NotNull
    @Size(max = 10)
    private String processStageCode;
    @Size(max = 10)
    private String status;

    public CounterProcessMapping(String organizationCode, String departmentCode, String tokenCategoryCode, String tokenTypeCode, String counterCode, String processStageCode, String status) {
        this.organizationCode = organizationCode;
        this.departmentCode = departmentCode;
        this.tokenCategoryCode = tokenCategoryCode;
        this.tokenTypeCode = tokenTypeCode;
        this.counterCode = counterCode;
        this.processStageCode = processStageCode;
        this.status = status;
    }
}
