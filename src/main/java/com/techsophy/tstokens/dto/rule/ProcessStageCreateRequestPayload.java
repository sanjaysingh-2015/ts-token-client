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
public class ProcessStageCreateRequestPayload {
    @Size(max = 10)
    private String organizationCode;
    @Size(max = 10)
    private String departmentCode;
    @Size(max = 10)
    private String tokenCategoryCode;
    @Size(max = 10)
    private String tokenTypeCode;
    @Size(max =10)
    private String code;
    @Size(max =10)
    private String name;
}
