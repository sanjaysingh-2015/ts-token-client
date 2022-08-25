package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.org.*;
import com.techsophy.tstokens.service.OrganizationService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OrganizationController.class)
class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrganizationService mockOrganizationService;

    @Test
    void testCreateOrganization() throws Exception {
        // Setup
        final OrganizationCreateRequestPayload requestPayload = createRequestData();
        final OrganizationResponsePayload responsePayload = createResponseData();
        when(mockOrganizationService.createOrganization(any(OrganizationCreateRequestPayload.class)))
                .thenReturn(responsePayload);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(post("/organization")
                        .content((new JSONObject(requestPayload)).toString()).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testUpdateOrganization() throws Exception {
        // Setup
        // Configure OrganizationService.updateOrganization(...).
        final OrganizationUpdateRequestPayload requestPayload = updateRequestData();
        final OrganizationResponsePayload responsePayload = createResponseData();
        when(mockOrganizationService.updateOrganization(anyString(),
                any(OrganizationUpdateRequestPayload.class))).thenReturn(responsePayload);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(put("/organization/{org-code}", "CODE")
                        .content((new JSONObject(requestPayload)).toString()).contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testGetOrganizationList() throws Exception {
        // Setup
        // Configure OrganizationService.getOrganizationList(...).
        final List<OrganizationResponsePayload> organizationResponsePayloads = List.of(createResponseData());
        when(mockOrganizationService.getOrganizationList()).thenReturn(organizationResponsePayloads);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/organization")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testGetOrganizationList_OrganizationServiceReturnsNoItems() throws Exception {
        // Setup
        when(mockOrganizationService.getOrganizationList()).thenReturn(Collections.emptyList());

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/organization")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void testGetOrganizationDetails() throws Exception {
        // Setup
        // Configure OrganizationService.getOrganizationDetails(...).
        final OrganizationResponsePayload organizationResponsePayload = createResponseData();
        when(mockOrganizationService.getOrganizationDetails(anyString())).thenReturn(organizationResponsePayload);

        // Run the test
        final MockHttpServletResponse response = mockMvc.perform(get("/organization/{org-code}", "CODE")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Verify the results
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    private OrganizationCreateRequestPayload createRequestData() {
        TokenTypeCreateRequestPayload tokenType = new TokenTypeCreateRequestPayload("CODE", "NAME","TT");
        List<TokenTypeCreateRequestPayload> tokenTypeList = new ArrayList<>();
        tokenTypeList.add(tokenType);
        TokenCategoryCreateRequestPayload tokenCategory = new TokenCategoryCreateRequestPayload(tokenTypeList,"CODE", "NAME","TC");
        List<TokenCategoryCreateRequestPayload> tokenCategoryList = new ArrayList<>();
        tokenCategoryList.add(tokenCategory);
        DepartmentCreateRequestPayload department = new DepartmentCreateRequestPayload(tokenCategoryList, "CODE", "NAME","DT");
        List<DepartmentCreateRequestPayload> departmentList = new ArrayList<>();
        departmentList.add(department);
        return new OrganizationCreateRequestPayload(departmentList,"CODE","NAME","ADDRESS", "CITY", "STATE", "COUNTRY", "OG");
    }
    private OrganizationResponsePayload createResponseData() {
        TokenTypeResponsePayload tokenType = new TokenTypeResponsePayload("ID","CATCODE","CODE", "NAME","TT", new Date(), "CREATED", new Date());
        List<TokenTypeResponsePayload> tokenTypeList = new ArrayList<>();
        tokenTypeList.add(tokenType);
        TokenCategoryResponsePayload tokenCategory = new TokenCategoryResponsePayload(tokenTypeList,"ID","CATCODE","CODE", "NAME","TC", new Date(), "CREATED", new Date());
        List<TokenCategoryResponsePayload> tokenCategoryList = new ArrayList<>();
        tokenCategoryList.add(tokenCategory);
        DepartmentResponsePayload department = new DepartmentResponsePayload(tokenCategoryList, "ID","CATCODE", "CODE", "NAME","DT", new Date(), "CREATED", new Date());
        List<DepartmentResponsePayload> departmentList = new ArrayList<>();
        departmentList.add(department);
        return new OrganizationResponsePayload(departmentList,"ID","CODE","NAME","ADDRESS", "CITY", "STATE", "COUNTRY", "OG", new Date(), "CREATED", new Date());
    }
    private OrganizationUpdateRequestPayload updateRequestData() {
        TokenTypeUpdateRequestPayload tokenType = new TokenTypeUpdateRequestPayload("CODE", "NAME","TT", "STATUS");
        List<TokenTypeUpdateRequestPayload> tokenTypeList = new ArrayList<>();
        tokenTypeList.add(tokenType);
        TokenCategoryUpdateRequestPayload tokenCategory = new TokenCategoryUpdateRequestPayload(tokenTypeList,"CODE", "NAME","TC", "STATUS");
        List<TokenCategoryUpdateRequestPayload> tokenCategoryList = new ArrayList<>();
        tokenCategoryList.add(tokenCategory);
        DepartmentUpdateRequestPayload department = new DepartmentUpdateRequestPayload(tokenCategoryList, "CODE", "NAME","DT", "STATUS");
        List<DepartmentUpdateRequestPayload> departmentList = new ArrayList<>();
        departmentList.add(department);
        return new OrganizationUpdateRequestPayload(departmentList,"CODE","NAME","ADDRESS", "CITY", "STATE", "COUNTRY", "OG", "STATUS");
    }
}
