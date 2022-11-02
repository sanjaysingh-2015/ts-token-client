package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.common.MasterLookupPayload;
import com.techsophy.tstokens.dto.rule.DeviceResponsePayload;
import com.techsophy.tstokens.service.MasterLookupService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/lookup")
public class MasterLookupController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MasterLookupService masterLookupService;

    public MasterLookupController(MasterLookupService masterLookupService) {
        this.masterLookupService = masterLookupService;
    }

    @GetMapping(value = {"/{lookup-type}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getMasterLookupData(
            @PathVariable(name = "lookup-type") String lookupType,
            @RequestParam(name = "org-code", required = false) String orgCode,
            @RequestParam(name = "dept-code", required = false) String deptCode,
            @RequestParam(name = "cat-code", required = false) String catCode,
            @RequestParam(name = "token-type-code", required = false) String tokenTypeCode) {
        logger.info("In getMasterLookupData()");
        MasterLookupPayload response = masterLookupService.getMasterLookupData(lookupType, orgCode, deptCode, catCode, tokenTypeCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Data Fetched Successfully"));
    }
}
