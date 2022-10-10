package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.api.TokenApiRequestPayload;
import com.techsophy.tstokens.dto.api.TokenApiResponsePayload;
import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.service.WorkingTokenService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Validated
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/operation/token")
public class TokenController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final WorkingTokenService workingTokenService;

    @Autowired
    public TokenController(WorkingTokenService workingTokenService) {
        this.workingTokenService = workingTokenService;
    }

    @PostMapping(value = "/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> generateToken(@RequestBody @Valid TokenApiRequestPayload requestPayload) {
        logger.info("In createOrganization()");
        TokenApiResponsePayload response = workingTokenService.generateToken(requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Generated Successfully"));
    }
}
