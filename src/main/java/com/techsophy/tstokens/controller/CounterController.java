package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.rule.CounterCreateRequestPayload;
import com.techsophy.tstokens.dto.rule.CounterResponsePayload;
import com.techsophy.tstokens.dto.rule.CounterUpdateRequestPayload;
import com.techsophy.tstokens.service.CounterService;
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
@RequestMapping("/counters")
public class CounterController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CounterService counterService;

    @Autowired
    public CounterController(CounterService counterService) {
        this.counterService = counterService;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createCounter(@RequestBody @Valid CounterCreateRequestPayload requestPayload) {
        logger.info("In createCounter()");
        CounterResponsePayload response = counterService.createCounter(requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage Created Successfully with Code: " + response.getCounterNo()));
    }

    @PutMapping(value = "/{counter-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateCounter(@PathVariable("counter-code") String counterCode, @RequestBody @Valid CounterUpdateRequestPayload requestPayload) {
        logger.info("In updateCounter()");
        CounterResponsePayload response = counterService.updateCounter(counterCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage Updated Successfully with Code: " + response.getCounterNo()));
    }

    @GetMapping(value = {"", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getCounterList(
            @RequestParam(name = "org-code", required = false) String orgCode,
            @RequestParam(name = "dept-code", required = false) String deptCode,
            @RequestParam(name = "cat-code", required = false) String catCode,
            @RequestParam(name = "token-type-code", required = false) String tokenTypeCode) {
        logger.info("In getDepartmentList()");
        List<CounterResponsePayload> response = counterService.getCounterList(orgCode, deptCode, catCode, tokenTypeCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage Details Fetched Successfully"));
    }

    @GetMapping(value = "/{counter-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getCounterDetails(@PathVariable(name = "counter-code") String counterCode,
                                                          @RequestParam(name = "org-code", required = false) String orgCode,
                                                          @RequestParam(name = "dept-code", required = false) String deptCode,
                                                          @RequestParam(name = "cat-code", required = false) String catCode,
                                                          @RequestParam(name = "token-type-code", required = false) String tokenTypeCode) {
        logger.info("In getCounterDetails()");
        CounterResponsePayload response = counterService.getCounterDetails(orgCode, deptCode, catCode, tokenTypeCode, counterCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage List Fetched Successfully"));
    }

    @DeleteMapping(value = "/{counter-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getCounterDetails(@PathVariable("counter-id") String counterId) {
        logger.info("In getCounterDetails()");
        String response = counterService.deleteCounter(counterId);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage List Fetched Successfully"));
    }
}
