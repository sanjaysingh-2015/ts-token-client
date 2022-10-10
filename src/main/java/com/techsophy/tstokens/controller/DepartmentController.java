package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.org.DepartmentCreateRequestPayload;
import com.techsophy.tstokens.dto.org.DepartmentResponsePayload;
import com.techsophy.tstokens.dto.org.DepartmentUpdateRequestPayload;
import com.techsophy.tstokens.service.DepartmentService;
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
@RequestMapping("/departments")
public class DepartmentController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping(value = "/{org-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> createDepartment(@PathVariable(name = "org-code") String orgCode,@RequestBody @Valid DepartmentCreateRequestPayload requestPayload) {
        logger.info("In createOrganization()");
        DepartmentResponsePayload response = departmentService.createDepartment(orgCode,requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Department Created Successfully with Code: " + response.getCode()));
    }

    @PutMapping(value = "/{org-code}/{dept-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> updateOrganization(@PathVariable("org-code") String orgCode, @PathVariable("dept-code") String deptCode,@RequestBody @Valid DepartmentUpdateRequestPayload requestPayload) {
        logger.info("In updateOrganization()");
        DepartmentResponsePayload response = departmentService.updateDepartment(orgCode, deptCode, requestPayload);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization Updated Successfully with Code: " + response.getCode()));
    }

    @GetMapping(value ={"", "/{org-code}"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getOrganizationList(@PathVariable(name = "org-code", required = false) String orgCode) {
        logger.info("In getDepartmentList()");
        List<DepartmentResponsePayload> response = departmentService.getDepartmentList(orgCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Department Details Fetched Successfully"));
    }

    @GetMapping(value = "/{org-code}/{dept-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getOrganizationDetails(@PathVariable("org-code") String orgCode, @PathVariable("dept-code") String deptCode) {
        logger.info("In getOrganizationDetails()");
        DepartmentResponsePayload response = departmentService.getDepartmentDetails(orgCode, deptCode);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization List Fetched Successfully"));
    }

    @DeleteMapping(value = "/{dept-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getOrganizationDetails(@PathVariable("dept-id") String deptId) {
        logger.info("In getOrganizationDetails()");
        String response = departmentService.deleteDepartment(deptId);
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Organization List Fetched Successfully"));
    }
}
