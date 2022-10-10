package com.techsophy.tstokens.security.services;

import com.techsophy.tstokens.entity.Organization;
import com.techsophy.tstokens.entity.User;
import com.techsophy.tstokens.repository.OrganizationRepository;
import com.techsophy.tstokens.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  OrganizationRepository organizationRepository;
  @Autowired
  UserRepository userRepository;

  @Transactional
  public UserDetails loadUserByOrganizationCode(String username) throws UsernameNotFoundException {
    Organization organization = organizationRepository.findByCodeAndStatus(username, "ACTIVE")
        .orElseThrow(() -> new UsernameNotFoundException("Organization Not Found with username: " + username));

    return UserDetailsImpl.build(organization);
  }

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }
}
