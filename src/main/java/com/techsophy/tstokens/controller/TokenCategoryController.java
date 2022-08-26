package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.org.*;
import com.techsophy.tstokens.service.DepartmentService;
import com.techsophy.tstokens.service.TokenCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/token-cat")
public class TokenCategoryController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final TokenCategoryService tokenCategoryService;

    @Autowired
    public TokenCategoryController(TokenCategoryService tokenCategoryService) {
        this.tokenCategoryService = tokenCategoryService;
    }

    @PostMapping(value = "/{org-code}/{dept-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createTokenCategory(@PathVariable("org-code") String orgCode,@PathVariable("dept-code") String deptCode,@RequestBody @Valid TokenCategoryCreateRequestPayload requestPayload) {
        logger.info("In createOrganization()");
        TokenCategoryResponsePayload response = tokenCategoryService.createTokenCategory(orgCode,deptCode,requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Token Category Created Successfully with Code: " + response.getCode()));
    }

    @PutMapping(value = "/{org-code}/{dept-code}/{token-cat-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateTokenCategory(@PathVariable("org-code") String orgCode, @PathVariable("dept-code") String deptCode, @PathVariable("toen-cat-code") String tokenCatCode, @RequestBody @Valid TokenCategoryUpdateRequestPayload requestPayload) {
        logger.info("In updateOrganization()");
        TokenCategoryResponsePayload response = tokenCategoryService.updateTokenCategory(orgCode, deptCode, tokenCatCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization Updated Successfully with Code: " + response.getCode()));
    }

    @GetMapping(value = "/{org-code}/{dept_code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getTokenCategoryList(@PathVariable("org-code") String orgCode,@PathVariable("dept-code") String deptCode) {
        logger.info("In getOrganizationList()");
        List<TokenCategoryResponsePayload> response = tokenCategoryService.getTokenCategoryList(orgCode, deptCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Department Details Fetched Successfully"));
    }

    @GetMapping(value = "/{org-code}/{dept-code}/{token-cat-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getTokenCategoryDetails(@PathVariable("org-code") String orgCode, @PathVariable("dept-code") String deptCode, @PathVariable("token-cat-code") String tokenCateCode) {
        logger.info("In getOrganizationDetails()");
        TokenCategoryResponsePayload response = tokenCategoryService.getTokenCategoryDetails(orgCode, deptCode, tokenCateCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization List Fetched Successfully"));
    }
}
