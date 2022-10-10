package com.techsophy.tstokens.dto.org;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class OrganizationResponsePayload {
    List<DepartmentResponsePayload> departments;
    private String id;
    private String code;
    private String name;
    private String email;
    private String phoneNo;
    private String firstName;
    private String middleName;
    private String lastName;
    private String address;
    private String city;
    private String state;
    private String country;
    private String tokenPrefix;
    private String authCode;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdOn;
    private String status;
    private Date updatedOn;
}
