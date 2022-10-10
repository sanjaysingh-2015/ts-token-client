package com.techsophy.tstokens.dto.master;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestPayload {
    @NotNull
    @Size(max = 50)
    private String firstName;
    @NotNull
    @Size(max = 50)
    private String lastName;
    @NotNull
    @Size(max = 255)
    private String email;
    @NotNull
    @Size(max = 255)
    private String password;
    @Size(max = 20)
    private String role;
}
