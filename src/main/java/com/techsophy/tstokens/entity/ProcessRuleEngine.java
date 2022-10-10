package com.techsophy.tstokens.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document("cf_process_rule_engine")
public class ProcessRuleEngine {
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
    private String currentStageCode;
    @NotNull
    private String currentStatusCode;
    @NotNull
    private String nextStageCode;
    @NotNull
    private String nextStatusCode;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdOn;
    @Size(max = 10)
    private String status;
}
