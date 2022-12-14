//package com.techsophy.tstokens.service;
//
//import com.techsophy.tstokens.dto.org.*;
//import com.techsophy.tstokens.entity.Department;
//import com.techsophy.tstokens.entity.Organization;
//import com.techsophy.tstokens.entity.TokenCategory;
//import com.techsophy.tstokens.entity.TokenType;
//import com.techsophy.tstokens.exception.ResourceNotFoundException;
//import com.techsophy.tstokens.repository.DepartmentRepository;
//import com.techsophy.tstokens.repository.OrganizationRepository;
//import com.techsophy.tstokens.repository.TokenCategoryRepository;
//import com.techsophy.tstokens.repository.TokenTypeRepository;
//import com.techsophy.tstokens.utils.ValidationUtils;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.*;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class OrganizationServiceTest {
//
//    @Mock
//    private OrganizationRepository mockOrganizationRepository;
//    @Mock
//    private DepartmentRepository mockDepartmentRepository;
//    @Mock
//    private TokenCategoryRepository mockTokenCategoryRepository;
//    @Mock
//    private TokenTypeRepository mockTokenTypeRepository;
//    @Mock
//    private ValidationUtils mockValidationUtils;
//    @Mock
//    private DepartmentService mockDepartmentService;
//    private OrganizationService organizationServiceUnderTest;
//
//
//
//    @BeforeEach
//    void setUp() {
//        organizationServiceUnderTest = new OrganizationService(mockOrganizationRepository, mockDepartmentRepository,
//                mockTokenCategoryRepository, mockTokenTypeRepository, mockValidationUtils,mockDepartmentService);
//    }
//
//    @Test
//    void testGetOrganizationDetails() {
//        // Setup
//        final OrganizationResponsePayload expectedResult = createResponseData();
//
//        // Configure OrganizationRepository.findByCode(...).
//        final Optional<Organization> organization = Optional.of(createOrganizationEntity());
//        when(mockOrganizationRepository.findByCode(anyString())).thenReturn(organization);
//
//        // Run the test
//        final OrganizationResponsePayload result = organizationServiceUnderTest.getOrganizationDetails("CODE");
//
//        // Verify the results
//        assertThat(result.getCode()).isEqualTo(expectedResult.getCode());
//    }
//
//    @Test
//    void testGetOrganizationDetails_OrganizationRepositoryReturnsAbsent() {
//        // Setup
//        final OrganizationResponsePayload expectedResult = null;
//        when(mockOrganizationRepository.findByCode("CODE20")).thenReturn(Optional.empty());
//
//        // Run the test
//        final OrganizationResponsePayload result = organizationServiceUnderTest.getOrganizationDetails("CODE20");
//
//        // Verify the results
//        assertThat(result).isEqualTo(expectedResult);
//    }
//
//    @Test
//    void testGetOrganizationList() {
//        // Setup
//        final List<OrganizationResponsePayload> expectedResult = List.of(createResponseData());
//
//        // Configure OrganizationRepository.findAll(...).
//        final List<Organization> organizations = List.of(createOrganizationEntity());
//        when(mockOrganizationRepository.findAll()).thenReturn(organizations);
//
//        // Run the test
//        final List<OrganizationResponsePayload> result = organizationServiceUnderTest.getOrganizationList();
//
//        // Verify the results
//        assertThat(result).hasSameSizeAs(expectedResult);
//    }
//
//    @Test
//    void testGetOrganizationList_OrganizationRepositoryReturnsNoItems() {
//        // Setup
//        final List<OrganizationResponsePayload> expectedResult = new ArrayList<>();
//        when(mockOrganizationRepository.findAll()).thenReturn(Collections.emptyList());
//
//        // Run the test
//        final List<OrganizationResponsePayload> result = organizationServiceUnderTest.getOrganizationList();
//
//        // Verify the results
//        assertThat(result).hasSameSizeAs(expectedResult);
//    }
//
//    @Test
//    void testCreateOrganization() {
//        // Setup
//        final OrganizationCreateRequestPayload requestPayload = createRequestData();
//        final OrganizationResponsePayload expectedResult = createResponseData();
//
//        // Configure OrganizationRepository.save(...).
//        final Organization organization = createOrganizationEntity();
//        when(mockOrganizationRepository.save(any(Organization.class))).thenReturn(organization);
//
//        // Configure DepartmentRepository.save(...).
//        final Department department = createDepartmentEntity();
//        when(mockDepartmentRepository.save(any(Department.class))).thenReturn(department);
//
//        // Configure TokenCategoryRepository.save(...).
//        final TokenCategory tokenCategory = createTokenCategoryEntity();
//        when(mockTokenCategoryRepository.save(any(TokenCategory.class))).thenReturn(tokenCategory);
//
//        // Configure TokenTypeRepository.save(...).
//        final TokenType tokenType = createTokenTypeEntity();
//        when(mockTokenTypeRepository.save(any(TokenType.class))).thenReturn(tokenType);
//
//
//        // Run the test
//        final OrganizationResponsePayload result = organizationServiceUnderTest.createOrganization(requestPayload);
//
//        // Verify the results
//        assertThat(result.getCode()).isEqualTo(expectedResult.getCode());
//    }
//
//    @Test
//    void testSaveOrganization() {
//        // Setup
//        final Organization organization = createOrganizationEntity();
//        final OrganizationResponsePayload expectedResult = createResponseData();
//
//        // Configure DepartmentRepository.save(...).
//        final Department department = createDepartmentEntity();
//        when(mockDepartmentRepository.save(any(Department.class))).thenReturn(department);
//
//        // Configure TokenCategoryRepository.save(...).
//        final TokenCategory tokenCategory = createTokenCategoryEntity();
//        when(mockTokenCategoryRepository.save(any(TokenCategory.class))).thenReturn(tokenCategory);
//
//        // Configure TokenTypeRepository.save(...).
//        final TokenType tokenType = createTokenTypeEntity();
//        when(mockTokenTypeRepository.save(any(TokenType.class))).thenReturn(tokenType);
//
//        // Configure OrganizationRepository.save(...).
//        final Organization organization1 = createOrganizationEntity();
//        when(mockOrganizationRepository.save(any(Organization.class))).thenReturn(organization1);
//
//        // Run the test
//        final OrganizationResponsePayload result = organizationServiceUnderTest.saveOrganization(organization);
//
//        // Verify the results
//        assertThat(result.getCode()).isEqualTo(expectedResult.getCode());
//    }
//
////    @Test
////    void testSaveDepartment() {
////        // Setup
////        final Organization organization = createOrganizationEntity();
////        final Department department = createDepartmentEntity();
////        final DepartmentResponsePayload expectedResult = createDepartmentResponseData();
////
////        // Configure DepartmentRepository.save(...).
////        final Department department1 = createDepartmentEntity();
////        when(mockDepartmentRepository.save(any(Department.class))).thenReturn(department1);
////
////        // Configure TokenCategoryRepository.save(...).
////        final TokenCategory tokenCategory = createTokenCategoryEntity();
////        when(mockTokenCategoryRepository.save(any(TokenCategory.class))).thenReturn(tokenCategory);
////
////        // Configure TokenTypeRepository.save(...).
////        final TokenType tokenType = createTokenTypeEntity();
////        when(mockTokenTypeRepository.save(any(TokenType.class))).thenReturn(tokenType);
////
////        // Run the test
////        final DepartmentResponsePayload result = organizationServiceUnderTest.saveDepartment(organization, department);
////
////        // Verify the results
////        assertThat(result.getCode()).isEqualTo(expectedResult.getCode());
////    }
////
////    @Test
////    void testSaveTokenCategory() {
////        // Setup
////        final Organization organization = createOrganizationEntity();
////        final Department department = createDepartmentEntity();
////        final TokenCategory tokenCategory = createTokenCategoryEntity();
////        final TokenCategoryResponsePayload expectedResult = createTokenCategoryResponseData();
////
////        // Configure TokenCategoryRepository.save(...).
////        final TokenCategory tokenCategory1 = createTokenCategoryEntity();
////        when(mockTokenCategoryRepository.save(any(TokenCategory.class))).thenReturn(tokenCategory1);
////
////        // Configure TokenTypeRepository.save(...).
////        final TokenType tokenType = createTokenTypeEntity();
////        when(mockTokenTypeRepository.save(any(TokenType.class))).thenReturn(tokenType);
////
////        // Run the test
////        final TokenCategoryResponsePayload result = organizationServiceUnderTest.saveTokenCategory(organization,
////                department, tokenCategory);
////
////        // Verify the results
////        assertThat(result.getCode()).isEqualTo(expectedResult.getCode());
////    }
////
////    @Test
////    void testSaveTokenType() {
////        // Setup
////        final Organization organization = createOrganizationEntity();
////        final Department department = createDepartmentEntity();
////        final TokenCategory tokenCategory = createTokenCategoryEntity();
////        final TokenType tokenType = createTokenTypeEntity();
////        final TokenTypeResponsePayload expectedResult = createTokenTypeResponseData();
////
////        // Configure TokenTypeRepository.save(...).
////        final TokenType tokenType1 = createTokenTypeEntity();
////        when(mockTokenTypeRepository.save(
////                any(TokenType.class))).thenReturn(tokenType1);
////
////        // Run the test
////        final TokenTypeResponsePayload result = organizationServiceUnderTest.saveTokenType(organization, department,
////                tokenCategory, tokenType);
////
////        // Verify the results
////        assertThat(result.getCode()).isEqualTo(expectedResult.getCode());
////    }
//
//    @Test
//    void testUpdateOrganization() {
//        // Setup
//        final OrganizationUpdateRequestPayload requestPayload = updateRequestData();
//        final OrganizationResponsePayload expectedResult = createResponseData();
//
//        // Configure OrganizationRepository.findByCode(...).
//        final Optional<Organization> organization = Optional.of(createOrganizationEntity());
//        when(mockOrganizationRepository.findByCode(anyString())).thenReturn(organization);
//
//        // Configure OrganizationRepository.save(...).
//        final Organization organization1 = createOrganizationEntity();
//        when(mockOrganizationRepository.save(any(Organization.class))).thenReturn(organization1);
//
//        // Configure DepartmentRepository.findByOrganizationCodeAndCode(...).
//        final Optional<Department> department = Optional.of(createDepartmentEntity());
//        when(mockDepartmentRepository.findByOrganizationCodeAndCode(anyString(), anyString())).thenReturn(department);
//
//        // Configure DepartmentRepository.save(...).
//        final Department department1 = createDepartmentEntity();
//        when(mockDepartmentRepository.save(any(Department.class))).thenReturn(department1);
//
//        // Configure TokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(...).
//        final Optional<TokenCategory> tokenCategory = Optional.of(createTokenCategoryEntity());
//        when(mockTokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(anyString(), anyString(), anyString())).thenReturn(tokenCategory);
//
//        // Configure TokenCategoryRepository.save(...).
//        final TokenCategory tokenCategory1 = createTokenCategoryEntity();
//        when(mockTokenCategoryRepository.save(any(TokenCategory.class))).thenReturn(tokenCategory1);
//
//        // Configure TokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(...).
//        final Optional<TokenType> tokenType = Optional.of(createTokenTypeEntity());
//        when(mockTokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(anyString(),
//                anyString(), anyString(), anyString())).thenReturn(tokenType);
//
//        // Configure TokenTypeRepository.save(...).
//        final TokenType tokenType1 = createTokenTypeEntity();
//        when(mockTokenTypeRepository.save(any(TokenType.class))).thenReturn(tokenType1);
//
//        // Run the test
//        final OrganizationResponsePayload result = organizationServiceUnderTest.updateOrganization("orgCode",
//                requestPayload);
//
//        // Verify the results
//        assertThat(result.getCode()).isEqualTo(expectedResult.getCode());
//    }
//
//    @Test
//    void testUpdateOrganization_OrganizationRepositoryFindByCodeReturnsAbsent() {
//        // Setup
//        final OrganizationUpdateRequestPayload requestPayload = null;
//        when(mockOrganizationRepository.findByCode(anyString())).thenReturn(Optional.empty());
//
//        // Run the test
//        assertThatThrownBy(
//                () -> organizationServiceUnderTest.updateOrganization("orgCode", requestPayload))
//                .isInstanceOf(ResourceNotFoundException.class);
//    }
//
//    @Test
//    void testUpdateDepartment() {
//        // Setup
//        final DepartmentUpdateRequestPayload requestPayload = updateDepartmentRequestData();
//        final DepartmentResponsePayload expectedResult = createDepartmentResponseData();
//        // Configure DepartmentRepository.findByOrganizationCodeAndCode(...).
//        final Optional<Department> department = Optional.of(createDepartmentEntity());
//        when(mockDepartmentRepository.findByOrganizationCodeAndCode(anyString(), anyString())).thenReturn(department);
//
//        // Configure DepartmentRepository.save(...).
//        final Department department1 = createDepartmentEntity();
//        when(mockDepartmentRepository.save(any(Department.class))).thenReturn(department1);
//
//        // Configure TokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(...).
//        final Optional<TokenCategory> tokenCategory = Optional.of(createTokenCategoryEntity());
//        when(mockTokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCode(anyString(), anyString(),
//                anyString())).thenReturn(tokenCategory);
//
//        // Configure TokenCategoryRepository.save(...).
//        final TokenCategory tokenCategory1 = createTokenCategoryEntity();
//        when(mockTokenCategoryRepository.save(any(TokenCategory.class))).thenReturn(tokenCategory1);
//
//        // Configure TokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(...).
//        final Optional<TokenType> tokenType = Optional.of(createTokenTypeEntity());
//        when(mockTokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(anyString(),
//                anyString(), anyString(), anyString())).thenReturn(tokenType);
//
//        // Configure TokenTypeRepository.save(...).
//        final TokenType tokenType1 = createTokenTypeEntity();
//        when(mockTokenTypeRepository.save(any(TokenType.class))).thenReturn(tokenType1);
//
//        // Run the test
//        final DepartmentResponsePayload result = organizationServiceUnderTest.updateDepartment("orgCode", "deptCode",
//                requestPayload);
//
//        // Verify the results
//        assertThat(result.getCode()).isEqualTo(expectedResult.getCode());
//    }
//
//    private OrganizationCreateRequestPayload createRequestData() {
//        TokenTypeCreateRequestPayload tokenType = new TokenTypeCreateRequestPayload("CODE", "NAME","TT");
//        List<TokenTypeCreateRequestPayload> tokenTypeList = new ArrayList<>();
//        tokenTypeList.add(tokenType);
//        TokenCategoryCreateRequestPayload tokenCategory = new TokenCategoryCreateRequestPayload(tokenTypeList,"CODE", "NAME","TC");
//        List<TokenCategoryCreateRequestPayload> tokenCategoryList = new ArrayList<>();
//        tokenCategoryList.add(tokenCategory);
//        DepartmentCreateRequestPayload department = new DepartmentCreateRequestPayload(tokenCategoryList, "CODE", "NAME","DT");
//        List<DepartmentCreateRequestPayload> departmentList = new ArrayList<>();
//        departmentList.add(department);
//        return new OrganizationCreateRequestPayload(departmentList,"CODE","NAME","ADDRESS", "CITY", "STATE", "COUNTRY", "OG");
//    }
//    private OrganizationResponsePayload createResponseData() {
//        TokenTypeResponsePayload tokenType = new TokenTypeResponsePayload("ID","CODE","CODE", "NAME","TT", new Date(), "CREATED", new Date());
//        List<TokenTypeResponsePayload> tokenTypeList = new ArrayList<>();
//        tokenTypeList.add(tokenType);
//        TokenCategoryResponsePayload tokenCategory = new TokenCategoryResponsePayload(tokenTypeList,"ID","CODE","CODE", "NAME","TC", new Date(), "CREATED", new Date());
//        List<TokenCategoryResponsePayload> tokenCategoryList = new ArrayList<>();
//        tokenCategoryList.add(tokenCategory);
//        DepartmentResponsePayload department = new DepartmentResponsePayload(tokenCategoryList, "ID","CODE", "CODE", "NAME","DT", new Date(), "CREATED", new Date());
//        List<DepartmentResponsePayload> departmentList = new ArrayList<>();
//        departmentList.add(department);
//        return new OrganizationResponsePayload(departmentList,"ID","CODE","NAME","ADDRESS", "CITY", "STATE", "COUNTRY", "OG", new Date(), "CREATED", new Date());
//    }
//    private DepartmentResponsePayload createDepartmentResponseData() {
//        TokenTypeResponsePayload tokenType = new TokenTypeResponsePayload("ID","CODE","CODE", "NAME","TT", new Date(), "CREATED", new Date());
//        List<TokenTypeResponsePayload> tokenTypeList = new ArrayList<>();
//        tokenTypeList.add(tokenType);
//        TokenCategoryResponsePayload tokenCategory = new TokenCategoryResponsePayload(tokenTypeList,"ID","CODE","CODE", "NAME","TC", new Date(), "CREATED", new Date());
//        List<TokenCategoryResponsePayload> tokenCategoryList = new ArrayList<>();
//        tokenCategoryList.add(tokenCategory);
//        return new DepartmentResponsePayload(tokenCategoryList, "ID","CODE", "CODE", "NAME","DT", new Date(), "CREATED", new Date());
//    }
//    private TokenCategoryResponsePayload createTokenCategoryResponseData() {
//        TokenTypeResponsePayload tokenType = new TokenTypeResponsePayload("ID","CODE","CODE", "NAME","TT", new Date(), "CREATED", new Date());
//        List<TokenTypeResponsePayload> tokenTypeList = new ArrayList<>();
//        tokenTypeList.add(tokenType);
//        return new TokenCategoryResponsePayload(tokenTypeList,"ID","CODE","CODE", "NAME","TC", new Date(), "CREATED", new Date());
//    }
//    private TokenTypeResponsePayload createTokenTypeResponseData() {
//        return new TokenTypeResponsePayload("ID","CODE","CODE", "NAME","TT", new Date(), "CREATED", new Date());
//    }
//    private OrganizationUpdateRequestPayload updateRequestData() {
//        TokenTypeUpdateRequestPayload tokenType = new TokenTypeUpdateRequestPayload("CODE", "NAME","TT", "STATUS");
//        List<TokenTypeUpdateRequestPayload> tokenTypeList = new ArrayList<>();
//        tokenTypeList.add(tokenType);
//        TokenCategoryUpdateRequestPayload tokenCategory = new TokenCategoryUpdateRequestPayload(tokenTypeList,"CODE", "NAME","TC", "STATUS");
//        List<TokenCategoryUpdateRequestPayload> tokenCategoryList = new ArrayList<>();
//        tokenCategoryList.add(tokenCategory);
//        DepartmentUpdateRequestPayload department = new DepartmentUpdateRequestPayload(tokenCategoryList, "CODE", "NAME","DT", "STATUS");
//        List<DepartmentUpdateRequestPayload> departmentList = new ArrayList<>();
//        departmentList.add(department);
//        return new OrganizationUpdateRequestPayload(departmentList,"CODE","NAME","ADDRESS", "CITY", "STATE", "COUNTRY", "OG", "STATUS");
//    }
//    private DepartmentUpdateRequestPayload updateDepartmentRequestData() {
//        TokenTypeUpdateRequestPayload tokenType = new TokenTypeUpdateRequestPayload("CODE", "NAME","TT", "STATUS");
//        List<TokenTypeUpdateRequestPayload> tokenTypeList = new ArrayList<>();
//        tokenTypeList.add(tokenType);
//        TokenCategoryUpdateRequestPayload tokenCategory = new TokenCategoryUpdateRequestPayload(tokenTypeList,"CODE", "NAME","TC", "STATUS");
//        List<TokenCategoryUpdateRequestPayload> tokenCategoryList = new ArrayList<>();
//        tokenCategoryList.add(tokenCategory);
//        return new DepartmentUpdateRequestPayload(tokenCategoryList, "CODE", "NAME","DT", "STATUS");
//    }
//
//    private TokenCategoryUpdateRequestPayload updateCategoryRequestData() {
//        TokenTypeUpdateRequestPayload tokenType = new TokenTypeUpdateRequestPayload("CODE", "NAME","TT", "STATUS");
//        List<TokenTypeUpdateRequestPayload> tokenTypeList = new ArrayList<>();
//        tokenTypeList.add(tokenType);
//        return new TokenCategoryUpdateRequestPayload(tokenTypeList,"CODE", "NAME","TC", "STATUS");
//    }
//    private TokenTypeUpdateRequestPayload updateTypeRequestData() {
//        return new TokenTypeUpdateRequestPayload("CODE", "NAME","TT", "STATUS");
//    }
//    private OrganizationUpdateRequestPayload updateRequestDataWithoutDepartment() {
//        return new OrganizationUpdateRequestPayload(null,"CODE","NAME","ADDRESS", "CITY", "STATE", "COUNTRY", "OG", "STATUS");
//    }
//    private OrganizationUpdateRequestPayload updateRequestDataWithoutCategory() {
//        DepartmentUpdateRequestPayload department = new DepartmentUpdateRequestPayload(null, "CODE", "NAME","DT", "STATUS");
//        List<DepartmentUpdateRequestPayload> departmentList = new ArrayList<>();
//        departmentList.add(department);
//        return new OrganizationUpdateRequestPayload(departmentList,"CODE","NAME","ADDRESS", "CITY", "STATE", "COUNTRY", "OG", "STATUS");
//    }
//    private OrganizationUpdateRequestPayload updateRequestDataWithoutType() {
//        TokenCategoryUpdateRequestPayload tokenCategory = new TokenCategoryUpdateRequestPayload(null,"CODE", "NAME","TC", "STATUS");
//        List<TokenCategoryUpdateRequestPayload> tokenCategoryList = new ArrayList<>();
//        tokenCategoryList.add(tokenCategory);
//        DepartmentUpdateRequestPayload department = new DepartmentUpdateRequestPayload(tokenCategoryList, "CODE", "NAME","DT", "STATUS");
//        List<DepartmentUpdateRequestPayload> departmentList = new ArrayList<>();
//        departmentList.add(department);
//        return new OrganizationUpdateRequestPayload(departmentList,"CODE","NAME","ADDRESS", "CITY", "STATE", "COUNTRY", "OG", "STATUS");
//    }
//    private TokenType createTokenTypeEntity() {
//        return new TokenType("ID","CODE", "CODE", "CODE", "CODE", "NAME", "TT",new Date(), "CREATED", new Date());
//    }
//    private TokenCategory createTokenCategoryEntity() {
//        List<TokenType> tokenTypeList = new ArrayList<>();
//        tokenTypeList.add(createTokenTypeEntity());
//        return new TokenCategory(tokenTypeList,"ID","CODE", "CODE",  "CODE", "NAME", "TC",new Date(), "CREATED", new Date());
//    }
//    private Department createDepartmentEntity() {
//        List<TokenCategory> tokenCategories = new ArrayList<>();
//        tokenCategories.add(createTokenCategoryEntity());
//        return new Department(tokenCategories, "ID", "CODE","CODE", "NAME", "DT",new Date(), "CREATED", new Date());
//    }
//    private Organization createOrganizationEntity() {
//        List<Department> departments = new ArrayList<>();
//        departments.add(createDepartmentEntity());
//        return new Organization(departments, "ID", "CODE", "NAME","ADDRESS", "CITY","STATE","COUNTRY","OG",new Date(), "CREATED", new Date());
//    }
//}
