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
public class DepartmentResponsePayload {
    List<TokenCategoryResponsePayload> tokenCategories;
    private String id;
    private String organizationCode;
    private String code;
    private String name;
    private String tokenPrefix;
    private Date createdOn;
    private String status;
    private Date updatedOn;
}
