package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.rule.CounterDeviceMapCreateRequestPayload;
import com.techsophy.tstokens.dto.rule.CounterDeviceMapResponsePayload;
import com.techsophy.tstokens.dto.rule.CounterDeviceMapUpdateRequestPayload;
import com.techsophy.tstokens.dto.rule.DeviceResponsePayload;
import com.techsophy.tstokens.service.CounterDeviceMappingService;
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
@RequestMapping("/counter-device-map")
public class CounterDeviceMappingController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CounterDeviceMappingService counterDeviceMappingService;
    @Autowired
    public CounterDeviceMappingController(CounterDeviceMappingService counterStageMappingService) {
        this.counterDeviceMappingService = counterStageMappingService;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createCounterStageMap(@RequestBody @Valid CounterDeviceMapCreateRequestPayload requestPayload) {
        logger.info("In createCounterStageMap()");
        CounterDeviceMapResponsePayload response = counterDeviceMappingService.addDeviceToCounterTree(requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Counter Process Stage Mapping Created Successfully with Code: " + response.getCounterCode()));
    }

    @PutMapping(value = "/{counter-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateCounter(@PathVariable("counter-code")String counterCode, @RequestBody @Valid CounterDeviceMapUpdateRequestPayload requestPayload) {
        logger.info("In updateCounter()");
        CounterDeviceMapResponsePayload response = counterDeviceMappingService.updateStatusProcessToCounterTree(requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Counter Process Stage Mapping Updated Successfully with Code: " + response.getCounterCode()));
    }

    @GetMapping(value = "/{counter-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getCounterList(
            @PathVariable(name = "counter-code", required = false) String counterCode,
            @RequestParam(name = "org-code", required = false) String orgCode,
            @RequestParam(name = "dept-code", required = false) String deptCode,
            @RequestParam(name = "cat-code", required = false) String catCode,
            @RequestParam(name = "token_type-code", required = false) String tokenTypeCode) {
        logger.info("In getDepartmentList()");
        List<DeviceResponsePayload> response = counterDeviceMappingService.getDeviceByCounterList(orgCode, deptCode, catCode, tokenTypeCode,counterCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Counter Process Stage Mapping Fetched Successfully"));
    }
}
