package com.techsophy.tstokens.dto.security.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
  @NotBlank
  @Size(max = 50)
  private String firstName;
  @NotBlank
  @Size(max = 50)
  private String lastName;
  @NotBlank
  @Size(min = 3, max = 20)
  private String username;
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;
  private Set<String> roles;
  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
}
