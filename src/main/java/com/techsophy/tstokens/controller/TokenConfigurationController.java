package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.api.TokenInitializeApiRequestPayload;
import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.service.TokenConfigurationService;
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
@RequestMapping("/operation/tokenConfig")
public class TokenConfigurationController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TokenConfigurationService tokenConfigurationService;

    @Autowired
    public TokenConfigurationController(TokenConfigurationService tokenConfigurationService) {
        this.tokenConfigurationService = tokenConfigurationService;
    }

    @PostMapping(value = "/init", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> initializeToken(@RequestBody @Valid TokenInitializeApiRequestPayload requestPayload) {
        logger.info("In createOrganization()");
        String response = tokenConfigurationService.initializeTokenForDate(requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, response));
    }

    @PutMapping(value = "/close", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateTokenType(@RequestBody @Valid TokenInitializeApiRequestPayload requestPayload) {
        logger.info("In updateOrganization()");
        String response = tokenConfigurationService.closeTokenForDate(requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, response));
    }
}
