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
public class CounterDeviceMapStatusPayload {
    @Size(max = 10)
    private String deviceUid;
    @Size(max = 10)
    private String status;
}
