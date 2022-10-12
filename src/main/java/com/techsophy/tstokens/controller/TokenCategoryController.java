package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.org.TokenCategoryCreateRequestPayload;
import com.techsophy.tstokens.dto.org.TokenCategoryResponsePayload;
import com.techsophy.tstokens.dto.org.TokenCategoryUpdateRequestPayload;
import com.techsophy.tstokens.service.TokenCategoryService;
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
@RequestMapping("/categories")
public class TokenCategoryController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TokenCategoryService tokenCategoryService;

    @Autowired
    public TokenCategoryController(TokenCategoryService tokenCategoryService) {
        this.tokenCategoryService = tokenCategoryService;
    }

    @PostMapping(value = "/{dept-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createTokenCategory(@PathVariable("dept-code") String deptCode, @RequestBody @Valid TokenCategoryCreateRequestPayload requestPayload) {
        logger.info("In createOrganization()");
        TokenCategoryResponsePayload response = tokenCategoryService.createTokenCategory(deptCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Category Created Successfully with Code: " + response.getCode()));
    }

    @PutMapping(value = "/{dept-code}/{token-cat-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateTokenCategory(@PathVariable("dept-code") String deptCode, @PathVariable("token-cat-code") String tokenCatCode, @RequestBody @Valid TokenCategoryUpdateRequestPayload requestPayload) {
        logger.info("In updateOrganization()");
        TokenCategoryResponsePayload response = tokenCategoryService.updateTokenCategory(deptCode, tokenCatCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization Updated Successfully with Code: " + response.getCode()));
    }

    @GetMapping(value = "/{dept-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getTokenCategoryList(@PathVariable("dept-code") String deptCode) {
        logger.info("In getOrganizationList()");
        List<TokenCategoryResponsePayload> response = tokenCategoryService.getTokenCategoryList(deptCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Department Details Fetched Successfully"));
    }

    @GetMapping(value = "/{dept-code}/{token-cat-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getTokenCategoryDetails(@PathVariable("dept-code") String deptCode, @PathVariable("token-cat-code") String tokenCateCode) {
        logger.info("In getOrganizationDetails()");
        TokenCategoryResponsePayload response = tokenCategoryService.getTokenCategoryDetails(deptCode, tokenCateCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization List Fetched Successfully"));
    }

    @DeleteMapping(value = "/{cat-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getOrganizationDetails(@PathVariable("cat-id") String catId) {
        logger.info("In getOrganizationDetails()");
        String response = tokenCategoryService.deleteCategory(catId);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization List Fetched Successfully"));
    }
}
