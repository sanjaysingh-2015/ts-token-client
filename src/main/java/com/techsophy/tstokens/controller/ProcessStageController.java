package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiErrorsResponse;
import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.rule.ProcessStageCreateRequestPayload;
import com.techsophy.tstokens.dto.rule.ProcessStageResponsePayload;
import com.techsophy.tstokens.dto.rule.ProcessStageUpdateRequestPayload;
import com.techsophy.tstokens.service.ProcessStageService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Validated
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/process-stages")
public class ProcessStageController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ProcessStageService processStageService;

    @Autowired
    public ProcessStageController(ProcessStageService processStageService) {
        this.processStageService = processStageService;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createProcessStage(@RequestBody @Valid ProcessStageCreateRequestPayload requestPayload) {
        logger.info("In createProcessStage()");
        ProcessStageResponsePayload response = processStageService.createProcessStage(requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage Created Successfully with Code: " + response.getCode()));
    }

    @PutMapping(value = "/{stage-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateProcessStage(@PathVariable("stage-code") String stageCode, @RequestBody @Valid ProcessStageUpdateRequestPayload requestPayload) {
        logger.info("In updateProcessStage()");
        ProcessStageResponsePayload response = processStageService.updateProcessStage(stageCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage Updated Successfully with Code: " + response.getCode()));
    }

    @GetMapping(value = {"", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getProcessStageList(
            @RequestParam(name = "org-code", required = false) String orgCode,
            @RequestParam(name = "dept-code", required = false) String deptCode,
            @RequestParam(name = "cat-code", required = false) String catCode,
            @RequestParam(name = "token-type-code", required = false) String tokenTypeCode) {
        logger.info("In getDepartmentList()");
        List<ProcessStageResponsePayload> response = processStageService.getProcessStageList(orgCode, deptCode, catCode, tokenTypeCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage Details Fetched Successfully"));
    }

    @GetMapping(value = "/{stage-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getProcessStageDetails(@PathVariable(name = "stage-code") String stageCode,
                                                               @RequestParam(name = "org-code", required = false) String orgCode,
                                                               @RequestParam(name = "dept-code", required = false) String deptCode,
                                                               @RequestParam(name = "cat-code", required = false) String catCode,
                                                               @RequestParam(name = "token_type-code", required = false) String tokenTypeCode) {
        logger.info("In getProcessStageDetails()");
        ProcessStageResponsePayload response = processStageService.getProcessStageDetails(orgCode, deptCode, catCode, tokenTypeCode, stageCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage List Fetched Successfully"));
    }

    @DeleteMapping(value = "/{stage-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getProcessStageDetails(@PathVariable("stage-id") String stageId) {
        logger.info("In getProcessStageDetails()");
        String response = processStageService.deleteProcessStage(stageId);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage List Fetched Successfully"));
    }

    @GetMapping(value ="/level", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getProcessStageListExact(
            @RequestParam(name = "org-code", required = false) String orgCode,
            @RequestParam(name = "dept-code", required = false) String deptCode,
            @RequestParam(name = "cat-code", required = false) String catCode,
            @RequestParam(name = "token-type-code", required = false) String tokenTypeCode) {
        logger.info("In getDepartmentList()");
        List<ProcessStageResponsePayload> response = processStageService.getProcessStageListForMapping(orgCode, deptCode, catCode, tokenTypeCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage Details Fetched Successfully"));
    }

//    private ApiErrorsResponse getValidationError(BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            List<FieldError> errors = bindingResult.getFieldErrors();
//            List<String> message = new ArrayList<>();
//
//            for (FieldError e : errors) {
//                message.add("@" + e.getField().toUpperCase() + ":" + e.getDefaultMessage());
//            }
//            ApiErrorsResponse error = new ApiErrorsResponse(null, Boolean.FALSE, message.toString());
//            return error;
//        }
//    }
}
