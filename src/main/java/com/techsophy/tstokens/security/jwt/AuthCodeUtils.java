package com.techsophy.tstokens.security.jwt;

import com.techsophy.tstokens.entity.Organization;
import com.techsophy.tstokens.exception.AccessDeniedException;
import com.techsophy.tstokens.exception.CustomAccessDeniedHandler;
import com.techsophy.tstokens.repository.OrganizationRepository;
import com.techsophy.tstokens.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Optional;

@Component
public class AuthCodeUtils {
  private static final Logger logger = LoggerFactory.getLogger(AuthCodeUtils.class);

  @Autowired
  private OrganizationRepository organizationRepository;
  public Organization validateAuthCode(String authToken) {
      Organization organization = organizationRepository.findByAuthCode(authToken).orElseThrow(() ->
              new AccessDeniedException("501: Invalid Auth Code for access client"));
      return organization;
  }
}
