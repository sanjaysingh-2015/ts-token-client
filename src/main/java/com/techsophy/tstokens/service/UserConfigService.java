package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.master.UserCreateRequestPayload;
import com.techsophy.tstokens.dto.master.UserResponsePayload;
import com.techsophy.tstokens.entity.User;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.UserRepository;
import com.techsophy.tstokens.utils.ApplicationMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserConfigService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String CREATED = "ACTIVE";

    @Autowired
    public UserConfigService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponsePayload getUserDetails(String email) {
        logger.info("In getUserDetails()");
        Optional<User> userOpt = userRepository.findByEmail(email);
        UserResponsePayload response = null;
        ApplicationMapping<UserResponsePayload, User> responseMapping = new ApplicationMapping<>();
        if (userOpt.isPresent()) {
            response = responseMapping.convert(userOpt.get(), UserResponsePayload.class);
        }
        return response;
    }

    public List<UserResponsePayload> getUserList() {
        logger.info("In getUserList()");
        List<User> userList = userRepository.findAll();
        List<UserResponsePayload> response = new ArrayList<>();
        ApplicationMapping<UserResponsePayload, User> responseMapping = new ApplicationMapping<>();
        userList.forEach(user ->
                response.add(responseMapping.convert(user, UserResponsePayload.class))
        );
        return response;
    }

    public UserResponsePayload createUser(UserCreateRequestPayload requestPayload) {
        logger.info("In createUser()");
        Map<String, String> errors = new HashMap<>();
//        validationUtils.validateTokenType(orgCode,deptCode,tokenCatCode,requestPayload, errors);
        ApplicationMapping<User, UserCreateRequestPayload> mapping = new ApplicationMapping<>();
        User user = mapping.convert(requestPayload, User.class);
        return saveUser(user);
    }

    public UserResponsePayload saveUser(User user) {
        logger.info("In saveUser()");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        ApplicationMapping<UserResponsePayload, User> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(user, UserResponsePayload.class);
    }

    public UserResponsePayload updateUser(String email, UserCreateRequestPayload requestPayload) {
        logger.info("In updateUser()");
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new ResourceNotFoundException("Invalid User to update");
        }

        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(requestPayload.getPassword()));
        userRepository.save(user);
        ApplicationMapping<UserResponsePayload, User> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(user, UserResponsePayload.class);
    }
}
