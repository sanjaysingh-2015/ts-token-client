package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.rule.CounterCreateRequestPayload;
import com.techsophy.tstokens.dto.rule.CounterResponsePayload;
import com.techsophy.tstokens.dto.rule.CounterUpdateRequestPayload;
import com.techsophy.tstokens.entity.Counter;
import com.techsophy.tstokens.exception.ResourceNotFoundException;
import com.techsophy.tstokens.repository.*;
import com.techsophy.tstokens.utils.ApplicationMapping;
import com.techsophy.tstokens.utils.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CounterService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final OrganizationRepository organizationRepository;
    private final DepartmentRepository departmentRepository;
    private final TokenCategoryRepository tokenCategoryRepository;
    private final ValidationUtils validationUtils;
    private final TokenCategoryService tokenCategoryService;
    private final TokenTypeRepository tokenTypeRepository;
    private final CounterRepository counterRepository;
    private static final String CREATED = "ACTIVE";
    private static final String DELETED = "DELETED";

    @Autowired
    public CounterService(OrganizationRepository organizationRepository, DepartmentRepository departmentRepository, ValidationUtils validationUtils, TokenCategoryService tokenCategoryService, TokenTypeRepository tokenTypeRepository, CounterRepository counterRepository, TokenCategoryRepository tokenCategoryRepository) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.validationUtils = validationUtils;
        this.tokenCategoryService = tokenCategoryService;
        this.tokenTypeRepository = tokenTypeRepository;
        this.counterRepository = counterRepository;
        this.tokenCategoryRepository = tokenCategoryRepository;
    }

    public CounterResponsePayload getCounterDetails(String orgCode, String deptCode, String catCode, String tokenTypeCode, String counterNo) {
        logger.info("In getCounterDetails()");
        //TODO: Validation
        Optional<Counter> counterOpt = Optional.empty();
        if(!StringUtils.isEmpty(orgCode)) {
            if(!StringUtils.isEmpty(deptCode)) {
                if(!StringUtils.isEmpty(catCode)) {
                    if(!StringUtils.isEmpty(tokenTypeCode)) {
                        counterOpt = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(orgCode, deptCode, catCode, tokenTypeCode,counterNo);
                    } else {
                        counterOpt = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(orgCode, deptCode, catCode,counterNo);
                    }
                } else {
                    counterOpt = counterRepository.findByOrganizationCodeAndDepartmentCodeAndCode(orgCode, deptCode,counterNo);
                }
            } else {
                counterOpt = counterRepository.findByOrganizationCodeAndCode(orgCode,counterNo);
            }
        } else {
            counterOpt = counterRepository.findByCode(counterNo);
        }
        CounterResponsePayload response = null;
        ApplicationMapping<CounterResponsePayload, Counter> responseMapping = new ApplicationMapping<>();
        if(counterOpt.isPresent()) {
            response = responseMapping.convert(counterOpt.get(), CounterResponsePayload.class);
        } else {
            throw new ResourceNotFoundException("TS908- Invalid input, supplied data does not exists");
        }
        return response;
    }
    public List<CounterResponsePayload> getCounterList(String orgCode, String deptCode, String catCode, String tokenTypeCode) {
        logger.info("In getCounterDetails()");
        List<Counter> counterList;
        if (!StringUtils.isEmpty(orgCode)) {
            if (!StringUtils.isEmpty(deptCode)) {
                if (!StringUtils.isEmpty(catCode)) {
                    if (!StringUtils.isEmpty(tokenTypeCode)) {
                        counterList = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCode(orgCode, deptCode, catCode, tokenTypeCode);
                    } else {
                        counterList = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCode(orgCode, deptCode, catCode);
                    }
                } else {
                    counterList = counterRepository.findByOrganizationCodeAndDepartmentCode(orgCode, deptCode);
                }
            } else {
                counterList = counterRepository.findByOrganizationCode(orgCode);
            }
        } else {
            counterList = counterRepository.findAll();
        }
        List<CounterResponsePayload> response = new ArrayList<>();
        ApplicationMapping<CounterResponsePayload, Counter> responseMapping = new ApplicationMapping<>();
        if (!counterList.isEmpty()) {
            counterList.forEach(counter ->
                    response.add(responseMapping.convert(counter, CounterResponsePayload.class))
            );
        } else {
            throw new ResourceNotFoundException("TS908- Invalid input, supplied data does not exists");
        }
        return response;
    }
    public CounterResponsePayload createCounter(CounterCreateRequestPayload requestPayload) {
        logger.info("In createCounter()");

        Map<String, String> errors = new HashMap<>();
        //validationUtils.validateDepartment(requestPayload, errors);
        ApplicationMapping<Counter, CounterCreateRequestPayload> mapping = new ApplicationMapping<>();
        Counter counter = mapping.convert(requestPayload, Counter.class);
        return saveCounter(counter);
    }
    public CounterResponsePayload saveCounter(Counter counter) {
        logger.info("In saveCounter()");
        organizationRepository.findByCodeAndStatus(counter.getOrganizationCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Organization Code Provided"));
        departmentRepository.findByOrganizationCodeAndCodeAndStatus(counter.getOrganizationCode(), counter.getDepartmentCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Department Code Provided"));
        tokenCategoryRepository.findByOrganizationCodeAndDepartmentCodeAndCodeAndStatus(counter.getOrganizationCode(), counter.getDepartmentCode(), counter.getTokenCategoryCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Category Code Provided"));
        tokenTypeRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCodeAndStatus(counter.getOrganizationCode(), counter.getDepartmentCode(), counter.getTokenCategoryCode(),counter.getTokenTypeCode(), CREATED).orElseThrow(() -> new ResourceNotFoundException("Invalid Token Type Code Provided"));
        counter.setCreatedOn(new Date());
        counter.setStatus(CREATED);
        counterRepository.save(counter);

        ApplicationMapping<CounterResponsePayload, Counter> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(counter, CounterResponsePayload.class);
    }
    public CounterResponsePayload updateCounter(String code, CounterUpdateRequestPayload requestPayload) {
        logger.info("In updateCounter()");
        Optional<Counter> counterOpt = Optional.empty();
        if(!StringUtils.isEmpty(requestPayload.getOrganizationCode())) {
            if(!StringUtils.isEmpty(requestPayload.getDepartmentCode())) {
                if(!StringUtils.isEmpty(requestPayload.getTokenCategoryCode())) {
                    if(!StringUtils.isEmpty(requestPayload.getTokenTypeCode())) {
                        counterOpt = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndTokenTypeCodeAndCode(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(), requestPayload.getTokenCategoryCode(), requestPayload.getTokenTypeCode(), code);
                    } else {
                        counterOpt = counterRepository.findByOrganizationCodeAndDepartmentCodeAndTokenCategoryCodeAndCode(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(), requestPayload.getTokenCategoryCode(),code);
                    }
                } else {
                    counterOpt = counterRepository.findByOrganizationCodeAndDepartmentCodeAndCode(requestPayload.getOrganizationCode(), requestPayload.getDepartmentCode(),code);
                }
            } else {
                counterOpt = counterRepository.findByOrganizationCodeAndCode(requestPayload.getOrganizationCode(),code);
            }
        } else {
            counterOpt = counterRepository.findByCode(code);
        }
        Counter counter = new Counter();
        if(counterOpt.isPresent()) {
            counter = counterOpt.get();
            counter.setStatus(requestPayload.getStatus());
        } else {
            throw new ResourceNotFoundException("TS908- Invalid input, supplied data does not exists");
        }
        counter.setCounterNo(requestPayload.getCounterNo());
        counterRepository.save(counter);

        ApplicationMapping<CounterResponsePayload, Counter> responseMapping = new ApplicationMapping<>();
        return responseMapping.convert(counter, CounterResponsePayload.class);
    }
    public String deleteCounter(String stageId) {
        logger.info("In deleteCounter()");
        Counter counter = counterRepository.findById(stageId).orElseThrow(
                () -> new ResourceNotFoundException("Invalid Counter to delete"));
        counter.setStatus(DELETED);
        counterRepository.save(counter);
        return "Counter Deleted successfully";
    }
}
