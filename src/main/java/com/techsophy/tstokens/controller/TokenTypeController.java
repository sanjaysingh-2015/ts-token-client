package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.org.*;
import com.techsophy.tstokens.service.TokenCategoryService;
import com.techsophy.tstokens.service.TokenTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/token-type")
public class TokenTypeController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TokenTypeService tokenTypeService;

    @Autowired
    public TokenTypeController(TokenTypeService tokenTypeService) {
        this.tokenTypeService = tokenTypeService;
    }

    @PostMapping(value = "/{org-code}/{dept-code}/{token-cat-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createTokenType(@PathVariable("org-code") String orgCode,@PathVariable("dept-code") String deptCode,@PathVariable("token-cat-code") String tokenCatCode,@RequestBody @Valid TokenTypeCreateRequestPayload requestPayload) {
        logger.info("In createOrganization()");
        TokenTypeResponsePayload response = tokenTypeService.createTokenType(orgCode,deptCode,tokenCatCode,requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type Created Successfully with Code: " + response.getCode()));
    }

    @PutMapping(value = "/{org-code}/{dept-code}/{token-cat-code}/{token-type-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateTokenType(@PathVariable("org-code") String orgCode, @PathVariable("dept-code") String deptCode, @PathVariable("toen-cat-code") String tokenCatCode, @PathVariable("toen-type-code") String tokenTypeCode, @RequestBody @Valid TokenTypeUpdateRequestPayload requestPayload) {
        logger.info("In updateOrganization()");
        TokenTypeResponsePayload response = tokenTypeService.updateTokenType(orgCode, deptCode, tokenCatCode, tokenTypeCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type Updated Successfully with Code: " + response.getCode()));
    }

    @GetMapping(value = "/{org-code}/{dept_code}/{token-cat-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getTokenTypeList(@PathVariable("org-code") String orgCode,@PathVariable("dept-code") String deptCode,@PathVariable("token-cat-code") String tokenCatCode) {
        logger.info("In getOrganizationList()");
        List<TokenTypeResponsePayload> response = tokenTypeService.getTokenTypeList(orgCode, deptCode, tokenCatCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type Details Fetched Successfully"));
    }

    @GetMapping(value = "/{org-code}/{dept-code}/{token-cat-code}/{token-type-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getTokenTypeDetails(@PathVariable("org-code") String orgCode, @PathVariable("dept-code") String deptCode, @PathVariable("token-cat-code") String tokenCatCode, @PathVariable("toen-type-code") String tokenTypeCode) {
        logger.info("In getOrganizationDetails()");
        TokenTypeResponsePayload response = tokenTypeService.getTokenTypeDetails(orgCode, deptCode, tokenCatCode, tokenTypeCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Type List Fetched Successfully"));
    }
}
