package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.rule.DeviceCreateRequestPayload;
import com.techsophy.tstokens.dto.rule.DeviceResponsePayload;
import com.techsophy.tstokens.dto.rule.DeviceUpdateRequestPayload;
import com.techsophy.tstokens.service.DeviceService;
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
@RequestMapping("/devices")
public class DeviceController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createDevice(@RequestBody @Valid DeviceCreateRequestPayload requestPayload) {
        logger.info("In createDevice()");
        DeviceResponsePayload response = deviceService.createDevice(requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage Created Successfully with Code: " + response.getDeviceUid()));
    }

    @PutMapping(value = "/{device-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateDevice(@PathVariable("device-code") String deviceCode, @RequestBody @Valid DeviceUpdateRequestPayload requestPayload) {
        logger.info("In updateDevice()");
        DeviceResponsePayload response = deviceService.updateDevice(deviceCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage Updated Successfully with Code: " + response.getDeviceUid()));
    }

    @GetMapping(value = {"", ""}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getDeviceList(
            @RequestParam(name = "org-code", required = false) String orgCode,
            @RequestParam(name = "dept-code", required = false) String deptCode,
            @RequestParam(name = "cat-code", required = false) String catCode,
            @RequestParam(name = "token_type-code", required = false) String tokenTypeCode) {
        logger.info("In getDepartmentList()");
        List<DeviceResponsePayload> response = deviceService.getDeviceList(orgCode, deptCode, catCode, tokenTypeCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage Details Fetched Successfully"));
    }

    @GetMapping(value = "/{device-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getDeviceDetails(@PathVariable(name = "device-code") String deviceCode,
                                                         @RequestParam(name = "org-code", required = false) String orgCode,
                                                         @RequestParam(name = "dept-code", required = false) String deptCode,
                                                         @RequestParam(name = "cat-code", required = false) String catCode,
                                                         @RequestParam(name = "token_type-code", required = false) String tokenTypeCode) {
        logger.info("In getDeviceDetails()");
        DeviceResponsePayload response = deviceService.getDeviceDetails(orgCode, deptCode, catCode, tokenTypeCode, deviceCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage List Fetched Successfully"));
    }

    @DeleteMapping(value = "/{device-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getDeviceDetails(@PathVariable("device-id") String deviceId) {
        logger.info("In getDeviceDetails()");
        String response = deviceService.deleteDevice(deviceId);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Process Stage List Fetched Successfully"));
    }
}
