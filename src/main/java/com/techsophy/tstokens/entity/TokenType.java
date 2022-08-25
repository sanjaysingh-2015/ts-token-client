package com.techsophy.tstokens.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document("cf_token_types")
public class TokenType {
    @Id
    private String id;
    @NotNull
    @Size(max = 10)
    private String organizationCode;
    @NotNull
    @Size(max = 10)
    private String departmentCode;
    @NotNull
    @Size(max = 10)
    private String tokenCategoryCode;
    @NotNull
    @Size(max = 10)
    private String code;
    @NotNull
    @Size(max = 100)
    private String name;
    @Size(max = 10)
    private String tokenPrefix;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdOn;
    @Size(max = 10)
    private String status;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updatedOn;

}
