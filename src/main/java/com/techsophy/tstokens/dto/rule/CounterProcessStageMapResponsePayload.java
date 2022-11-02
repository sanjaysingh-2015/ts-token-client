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
public class CounterProcessStageMapResponsePayload {
    private String organizationCode;
    private String departmentCode;
    private String tokenCategoryCode;
    private String tokenTypeCode;
    private String counterCode;
    private List<CounterProcessStageMapStatusResponsePayload> processStage;
}
