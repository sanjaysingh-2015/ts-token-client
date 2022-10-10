package com.techsophy.tstokens.utils;

import com.techsophy.tstokens.dto.org.DepartmentCreateRequestPayload;
import com.techsophy.tstokens.dto.org.OrganizationCreateRequestPayload;
import com.techsophy.tstokens.dto.org.TokenCategoryCreateRequestPayload;
import com.techsophy.tstokens.dto.org.TokenTypeCreateRequestPayload;
import com.techsophy.tstokens.repository.DepartmentRepository;
import com.techsophy.tstokens.repository.OrganizationRepository;
import com.techsophy.tstokens.repository.TokenCategoryRepository;
import com.techsophy.tstokens.repository.TokenTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationUtilsTest {

    @Mock
    private OrganizationRepository mockOrganizationRepository;
    @Mock
    private DepartmentRepository mockDepartmentRepository;
    @Mock
    private TokenCategoryRepository mockTokenCategoryRepository;
    @Mock
    private TokenTypeRepository mockTokenTypeRepository;

    private ValidationUtils validationUtilsUnderTest;

    @BeforeEach
    void setUp() {
        validationUtilsUnderTest = new ValidationUtils(mockOrganizationRepository, mockDepartmentRepository,
                mockTokenCategoryRepository, mockTokenTypeRepository);
    }

    @Test
    void testValidateOrganization() {
        // Setup
        final OrganizationCreateRequestPayload requestPayload = createRequestData("CODE00", "NAME00");

        // Configure OrganizationRepository.findByCode(...).
        when(mockOrganizationRepository.findByCode(anyString())).thenReturn(Optional.empty());

        // Configure OrganizationRepository.findByName(...).
        when(mockOrganizationRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Configure DepartmentRepository.findByOrganizationCodeAndCode(...).
        when(mockDepartmentRepository.findByOrganizationCodeAndCode(anyString(), anyString())).thenReturn(Optional.empty());

        // Configure DepartmentRepository.findByOrganizationCodeAndName(...).
        when(mockDepartmentRepository.findByOrganizationCodeAndName(anyString(), anyString())).thenReturn(Optional.empty());

        // Configure TokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(...).
        when(mockTokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(anyString(), anyString(),
                anyString())).thenReturn(Optional.empty());

        // Configure TokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndName(...).
        when(mockTokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndName(anyString(), anyString(),
                anyString())).thenReturn(Optional.empty());

        // Configure TokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(...).
        when(mockTokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(anyString(), anyString(),
                anyString(), anyString())).thenReturn(Optional.empty());

        // Configure TokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndName(...).
        when(mockTokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndName(anyString(), anyString(),
                anyString(), anyString())).thenReturn(Optional.empty());

        // Run the test
        validationUtilsUnderTest.validateOrganization(requestPayload);

        // Verify the results
    }

    @Test
    void testValidateDepartment() {
        // Setup
        final DepartmentCreateRequestPayload requestPayload = createDepartmentRequestData();

        // Configure DepartmentRepository.findByOrganizationCodeAndName(...).
        when(mockDepartmentRepository.findByOrganizationCodeAndName(anyString(), anyString())).thenReturn(Optional.empty());

        // Configure TokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(...).
        when(mockTokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(anyString(), anyString(),
                anyString())).thenReturn(Optional.empty());

        // Configure TokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndName(...).
        when(mockTokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndName(anyString(), anyString(),
                anyString())).thenReturn(Optional.empty());

        // Configure TokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(...).
        when(mockTokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(anyString(), anyString(),
                anyString(), anyString())).thenReturn(Optional.empty());

        // Configure TokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndName(...).
        when(mockTokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndName(anyString(), anyString(),
                anyString(), anyString())).thenReturn(Optional.empty());
        final Map<String, String> errors = new HashMap<>();
        // Run the test
        validationUtilsUnderTest.validateDepartment(requestPayload, errors);

        // Verify the results
    }

    @Test
    void testValidateTokenCategory() {
        // Setup
        final TokenCategoryCreateRequestPayload requestPayload = createCategoryRequestData("CODE001", "NAME001");
        // Configure TokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(...).
        when(mockTokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(anyString(), anyString(),
                anyString())).thenReturn(Optional.empty());

        // Configure TokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndName(...).
        when(mockTokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndName(anyString(), anyString(),
                anyString())).thenReturn(Optional.empty());

        // Configure TokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(...).
        when(mockTokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(anyString(), anyString(),
                anyString(), anyString())).thenReturn(Optional.empty());

        // Configure TokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndName(...).
        when(mockTokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndName(anyString(), anyString(),
                anyString(), anyString())).thenReturn(Optional.empty());
        final Map<String, String> errors = new HashMap<>();

        // Run the test
        validationUtilsUnderTest.validateTokenCategory("orgCode", "deptCode", requestPayload, errors);

        // Verify the results
    }

    @Test
    void testValidateTokenType() {
        // Setup
        final TokenTypeCreateRequestPayload requestPayload = createTypeRequestData("CODE001", "NAME001");
        // Configure TokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(...).
        when(mockTokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(anyString(), anyString(),
                anyString(), anyString())).thenReturn(Optional.empty());

        // Configure TokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndName(...).
        when(mockTokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndName(anyString(), anyString(),
                anyString(), anyString())).thenReturn(Optional.empty());
        final Map<String, String> errors = new HashMap<>();

        // Run the test
        validationUtilsUnderTest.validateTokenType("catCode", requestPayload, errors);

        // Verify the results
    }

    private DepartmentCreateRequestPayload createDepartmentRequestData() {
        TokenTypeCreateRequestPayload tokenType = new TokenTypeCreateRequestPayload("CODE", "NAME","TT");
        List<TokenTypeCreateRequestPayload> tokenTypeList = new ArrayList<>();
        tokenTypeList.add(tokenType);
        TokenCategoryCreateRequestPayload tokenCategory = new TokenCategoryCreateRequestPayload(tokenTypeList,"CODE", "NAME","TC");
        List<TokenCategoryCreateRequestPayload> tokenCategoryList = new ArrayList<>();
        tokenCategoryList.add(tokenCategory);
        return new DepartmentCreateRequestPayload(tokenCategoryList, "CODE", "NAME","DT");
    }
    private OrganizationCreateRequestPayload createRequestData(String code, String name) {
        TokenTypeCreateRequestPayload tokenType = new TokenTypeCreateRequestPayload(code, name,"TT");
        List<TokenTypeCreateRequestPayload> tokenTypeList = new ArrayList<>();
        tokenTypeList.add(tokenType);
        TokenCategoryCreateRequestPayload tokenCategory = new TokenCategoryCreateRequestPayload(tokenTypeList,code, name,"TC");
        List<TokenCategoryCreateRequestPayload> tokenCategoryList = new ArrayList<>();
        tokenCategoryList.add(tokenCategory);
        DepartmentCreateRequestPayload department = new DepartmentCreateRequestPayload(tokenCategoryList, code, name,"DT");
        List<DepartmentCreateRequestPayload> departmentList = new ArrayList<>();
        departmentList.add(department);
        return new OrganizationCreateRequestPayload(departmentList,code, name,"ADDRESS", "CITY", "STATE", "COUNTRY", "OG");
    }
    private TokenCategoryCreateRequestPayload createCategoryRequestData(String code, String name) {
        TokenTypeCreateRequestPayload tokenType = new TokenTypeCreateRequestPayload(code, name,"TT");
        List<TokenTypeCreateRequestPayload> tokenTypeList = new ArrayList<>();
        tokenTypeList.add(tokenType);
        return new TokenCategoryCreateRequestPayload(tokenTypeList,code, name,"TC");
    }
    private TokenTypeCreateRequestPayload createTypeRequestData(String code, String name) {
        return new TokenTypeCreateRequestPayload(code, name,"TT");
    }
}
