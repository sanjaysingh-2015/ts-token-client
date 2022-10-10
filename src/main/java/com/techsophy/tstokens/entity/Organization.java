package com.techsophy.tstokens.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Document("cf_organizations")
public class Organization {
    @DocumentReference(lazy = true, lookup = "{ 'organizationCode' : ?#{#self.code} }")
    List<Department> departments;
    @Id
    private String id;
    @NotNull
    @Size(max = 10)
    private String code;
    @NotNull
    @Size(max = 100)
    private String name;
    @Email
    private String email;
    @NotNull
    private String phoneNo;
    @NotNull
    @Size(max=50)
    private String firstName;
    @Size(max=50)
    private String middleName;
    @NotNull
    @Size(max=50)
    private String lastName;
    @Size(max = 255)
    private String address;
    @Size(max = 100)
    private String city;
    @Size(max = 100)
    private String state;
    @Size(max = 100)
    private String country;
    @Size(max = 10)
    private String tokenPrefix;
    @NotNull
    private String authCode;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date createdOn;
    @Size(max = 10)
    private String status;
    @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private Date updatedOn;
}
