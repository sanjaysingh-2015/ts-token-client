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
public class ProcessRuleEngineResponsePayload {
    private String id;
    private String organizationCode;
    private String departmentCode;
    private String tokenCategoryCode;
    private String tokenTypeCode;
    private String currentStageCode;
    private String currentStatusCode;
    private String nextStageCode;
    private String nextStatusCode;
    private String status;
}
