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
    private String counterId;
    @NotNull
    private String processStageId;
    @Size(max = 10)
    private String status;

    public CounterProcessMapping(String organizationCode, String departmentCode, String tokenCategoryCode, String tokenTypeCode, String counterId, String processStageId, String status) {
        this.organizationCode = organizationCode;
        this.departmentCode = departmentCode;
        this.tokenCategoryCode = tokenCategoryCode;
        this.tokenTypeCode = tokenTypeCode;
        this.counterId = counterId;
        this.processStageId = processStageId;
        this.status = status;
    }
}
