package com.techsophy.tstokens.dto.api;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TokenApiResponsePayload {
    private String organizationCode;
    private String departmentCode;
    private String tokenCategoryCode;
    private String tokenTypeCode;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date workDate;
    private String tokenNo;
    private String tokenEntity;
    private String tokenEntityValue;
    private String counterNo;
    private String processStageCode;
    private String processStatusCode;
    private String userName;
}
