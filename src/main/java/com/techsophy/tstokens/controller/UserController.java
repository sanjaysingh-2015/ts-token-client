package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.master.UserCreateRequestPayload;
import com.techsophy.tstokens.dto.master.UserResponsePayload;
import com.techsophy.tstokens.service.UserConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserConfigService userConfigService;

    @Autowired
    public UserController(UserConfigService userConfigService) {
        this.userConfigService = userConfigService;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createUser(@RequestBody @Valid UserCreateRequestPayload requestPayload) {
        logger.info("In createUser()");
        UserResponsePayload response = userConfigService.createUser(requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type Created Successfully with Email: " + response.getEmail()));
    }

    @PutMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateUser(@PathVariable("email") String email, @RequestBody @Valid UserCreateRequestPayload requestPayload) {
        logger.info("In updateUser()");
        UserResponsePayload response = userConfigService.updateUser(email, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type Updated Successfully with Email: " + response.getEmail()));
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getUserList() {
        logger.info("In getUserList()");
        List<UserResponsePayload> response = userConfigService.getUserList();
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type Details Fetched Successfully"));
    }

    @GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getUserDetails(@PathVariable("email") String email) {
        logger.info("In getUserDetails()");
        UserResponsePayload response = userConfigService.getUserDetails(email);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type List Fetched Successfully"));
    }
}
