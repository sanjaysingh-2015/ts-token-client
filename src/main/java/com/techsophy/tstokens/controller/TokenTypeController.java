package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.org.TokenTypeCreateRequestPayload;
import com.techsophy.tstokens.dto.org.TokenTypeResponsePayload;
import com.techsophy.tstokens.dto.org.TokenTypeUpdateRequestPayload;
import com.techsophy.tstokens.service.TokenTypeService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/tokenTypes")
public class TokenTypeController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TokenTypeService tokenTypeService;

    @Autowired
    public TokenTypeController(TokenTypeService tokenTypeService) {
        this.tokenTypeService = tokenTypeService;
    }

    @PostMapping(value = "/{token-cat-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createTokenType(@PathVariable("token-cat-code") String tokenCatCode, @RequestBody @Valid TokenTypeCreateRequestPayload requestPayload) {
        logger.info("In createOrganization()");
        TokenTypeResponsePayload response = tokenTypeService.createTokenType(tokenCatCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type Created Successfully with Code: " + response.getCode()));
    }

    @PutMapping(value = "/{token-cat-code}/{token-type-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateTokenType(@PathVariable("token-cat-code") String tokenCatCode, @PathVariable("token-type-code") String tokenTypeCode, @RequestBody @Valid TokenTypeUpdateRequestPayload requestPayload) {
        logger.info("In updateOrganization()");
        TokenTypeResponsePayload response = tokenTypeService.updateTokenType(tokenCatCode, tokenTypeCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type Updated Successfully with Code: " + response.getCode()));
    }

    @GetMapping(value = "/{token-cat-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getTokenTypeList(@PathVariable("token-cat-code") String tokenCatCode) {
        logger.info("In getOrganizationList()");
        List<TokenTypeResponsePayload> response = tokenTypeService.getTokenTypeList(tokenCatCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type Details Fetched Successfully"));
    }

    @GetMapping(value = "/{token-cat-code}/{token-type-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getTokenTypeDetails(@PathVariable("token-cat-code") String tokenCatCode, @PathVariable("toen-type-code") String tokenTypeCode) {
        logger.info("In getOrganizationDetails()");
        TokenTypeResponsePayload response = tokenTypeService.getTokenTypeDetails(tokenCatCode, tokenTypeCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type List Fetched Successfully"));
    }

    @DeleteMapping(value = "/{type-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getOrganizationDetails(@PathVariable("type-id") String typeId) {
        logger.info("In getOrganizationDetails()");
        String response = tokenTypeService.deleteTokenType(typeId);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization List Fetched Successfully"));
    }
}
