package com.techsophy.tstokens.dto.api;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TokenInitializeApiResponsePayload {
    private String organizationCode;
    private String departmentCode;
    private String categoryCode;
    private String tokenTypeCode;
    private Date workDate;
    private String userName;
}
