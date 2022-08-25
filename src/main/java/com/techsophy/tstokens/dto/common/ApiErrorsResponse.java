package com.techsophy.tstokens.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiErrorsResponse implements IApiResponse {
    private Object errors;
    private Boolean success;
    private String message;

    public ApiErrorsResponse(Object errors, Boolean success, String message) {
        this.errors = errors;
        this.success = success;
        this.message = message;
    }

}
