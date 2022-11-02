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
public class CounterProcessStageMapStatusResponsePayload {
    private String id;
    @Size(max = 10)
    private String processStageCode;
    @Size(max = 10)
    private String status;
}
