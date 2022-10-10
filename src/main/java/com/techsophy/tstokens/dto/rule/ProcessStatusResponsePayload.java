package com.techsophy.tstokens.dto.rule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProcessStatusResponsePayload {
    private String id;
    private String organizationCode;
    private String departmentCode;
    private String tokenCategoryCode;
    private String tokenTypeCode;
    private String code;
    private String name;
    private String status;
}
