package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.rule.ProcessStatusCreateRequestPayload;
import com.techsophy.tstokens.dto.rule.ProcessStatusResponsePayload;
import com.techsophy.tstokens.dto.rule.ProcessStatusUpdateRequestPayload;
import com.techsophy.tstokens.service.ProcessStatusService;
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
@RequestMapping("/process-status")
public class ProcessStatusController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ProcessStatusService processStatusService;

    @Autowired
    public ProcessStatusController(ProcessStatusService processStatusService) {
        this.processStatusService = processStatusService;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createProcessStatus(@RequestBody @Valid ProcessStatusCreateRequestPayload requestPayload) {
        logger.info("In createProcessStatus()");
        ProcessStatusResponsePayload response = processStatusService.createProcessStatus(requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Status Created Successfully with Code: " + response.getCode()));
    }

    @PutMapping(value = "/{status-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateProcessStatus(@PathVariable("status-code") String statusCode,@RequestBody @Valid ProcessStatusUpdateRequestPayload requestPayload) {
        logger.info("In updateProcessStatus()");
        ProcessStatusResponsePayload response = processStatusService.updateProcessStatus(statusCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Status Updated Successfully with Code: " + response.getCode()));
    }

    @GetMapping(value ={"", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getProcessStatusList(
            @RequestParam(name = "org-code", required = false) String orgCode,
            @RequestParam(name = "dept-code", required = false) String deptCode,
            @RequestParam(name = "cat-code", required = false) String catCode,
            @RequestParam(name = "token_type-code", required = false) String tokenTypeCode) {
        logger.info("In getDepartmentList()");
        List<ProcessStatusResponsePayload> response = processStatusService.getProcessStatusList(orgCode, deptCode, catCode,tokenTypeCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Status Details Fetched Successfully"));
    }

    @GetMapping(value = "/{status-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getProcessStatusDetails(@PathVariable(name="status-code") String statusCode,
                                                               @RequestParam(name = "org-code", required = false) String orgCode,
                                                               @RequestParam(name = "dept-code", required = false) String deptCode,
                                                               @RequestParam(name = "cat-code", required = false) String catCode,
                                                               @RequestParam(name = "token_type-code", required = false) String tokenTypeCode) {
        logger.info("In getProcessStatusDetails()");
        ProcessStatusResponsePayload response = processStatusService.getProcessStatusDetails(orgCode, deptCode, catCode, tokenTypeCode, statusCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Status List Fetched Successfully"));
    }

    @DeleteMapping(value = "/{status-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getProcessStatusDetails(@PathVariable("status-id") String statusId) {
        logger.info("In getProcessStatusDetails()");
        String response = processStatusService.deleteProcessStatus(statusId);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Status List Fetched Successfully"));
    }
}
