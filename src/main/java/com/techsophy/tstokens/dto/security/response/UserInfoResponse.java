package com.techsophy.tstokens.dto.security.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {
  private String id;
  private String firstName;
  private String lastName;
  private String username;
  private String email;
  private String token;
  private List<String> roles;
}
