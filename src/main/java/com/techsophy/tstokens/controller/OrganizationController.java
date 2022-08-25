package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.org.OrganizationCreateRequestPayload;
import com.techsophy.tstokens.dto.org.OrganizationResponsePayload;
import com.techsophy.tstokens.dto.org.OrganizationUpdateRequestPayload;
import com.techsophy.tstokens.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/organization")
public class OrganizationController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createOrganization(@RequestBody @Valid OrganizationCreateRequestPayload requestPayload) {
        logger.info("In createOrganization()");
        OrganizationResponsePayload response = organizationService.createOrganization(requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization Created Successfully with Code: " + response.getCode()));
    }

    @PutMapping(value = "/{org-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateOrganization(@PathVariable("org-code") String orgCode, @RequestBody @Valid OrganizationUpdateRequestPayload requestPayload) {
        logger.info("In updateOrganization()");
        OrganizationResponsePayload response = organizationService.updateOrganization(orgCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization Updated Successfully with Code: " + response.getCode()));
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getOrganizationList() {
        logger.info("In getOrganizationList()");
        List<OrganizationResponsePayload> response = organizationService.getOrganizationList();
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization Details Fetched Successfully"));
    }

    @GetMapping(value = "/{org-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getOrganizationDetails(@PathVariable("org-code") String orgCode) {
        logger.info("In getOrganizationDetails()");
        OrganizationResponsePayload response = organizationService.getOrganizationDetails(orgCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization List Fetched Successfully"));
    }
}
