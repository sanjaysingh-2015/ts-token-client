package com.techsophy.tstokens.dto.org;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class TokenTypeResponsePayload {
    private String id;
    private String tokenCategoryCode;
    private String code;
    private String name;
    private String tokenPrefix;
    private Date createdOn;
    private String status;
    private Date updatedOn;
}
