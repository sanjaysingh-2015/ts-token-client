package com.techsophy.tstokens.dto.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessRuleEngineUpdateRequestPayload {
    @Size(max = 10)
    private String organizationCode;
    @Size(max = 10)
    private String departmentCode;
    @Size(max = 10)
    private String tokenCategoryCode;
    @Size(max = 10)
    private String tokenTypeCode;
    private String currentStageCode;
    @Size(max =10)
    private String currentStatusCode;
    @Size(max =10)
    private String nextStageCode;
    @Size(max =10)
    private String nextStatusCode;
    @Size(max =10)
    private String status;
}
