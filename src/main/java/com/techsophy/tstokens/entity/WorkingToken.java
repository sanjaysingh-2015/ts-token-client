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
@Document("ms_working_token")
public class WorkingToken {
    @Id
    private String id;
    @NotNull
    @Size(max = 10)
    private String organizationCode;
    @Size(max = 10)
    private String departmentCode;
    @Size(max = 10)
    private String tokenCategoryCode;
    @Size(max = 10)
    private String tokenTypeCode;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date workDate;
    @NotNull
    private String tokenNo;
    @NotNull
    private String tokenEntity;
    @NotNull
    private String userName;
    @NotNull
    private String tokenEntityValue;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdOn;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updatedOn;
}
