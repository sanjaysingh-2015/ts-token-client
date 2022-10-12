package com.techsophy.tstokens.dto.org;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationCreateRequestPayload {
    List<DepartmentCreateRequestPayload> departments;
    @NotNull
    @Size(max = 100)
    private String name;
    @Email
    private String email;
    @NotNull
    private String phoneNo;
    @NotNull
    @Size(max = 50)
    private String firstName;
    @Size(max = 50)
    private String middleName;
    @NotNull
    @Size(max = 50)
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
}
