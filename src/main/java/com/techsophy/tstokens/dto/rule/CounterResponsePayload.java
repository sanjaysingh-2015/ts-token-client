package com.techsophy.tstokens.dto.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CounterResponsePayload {
    private String id;
    private String organizationCode;
    private String departmentCode;
    private String tokenCategoryCode;
    private String tokenTypeCode;
    private String code;
    private String counterNo;
    private String status;
    private List<ProcessStageResponsePayload> mappedProcessStages;
}
