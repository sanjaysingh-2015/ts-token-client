package com.techsophy.tstokens.dto.org;

import lombok.*;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TokenCategoryResponsePayload {
    List<TokenTypeResponsePayload> tokenTypes;
    private String id;
    private String departmentCode;
    private String code;
    private String name;
    private String tokenPrefix;
    private Date createdOn;
    private String status;
    private Date updatedOn;
}
