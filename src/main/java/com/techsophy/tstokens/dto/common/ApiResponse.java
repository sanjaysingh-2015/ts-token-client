package com.techsophy.tstokens.dto.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponse implements IApiResponse {
    private Object data;
    private Boolean success;
    private String message;

    public ApiResponse(Object data, Boolean success, String message) {
        this.data = data;
        this.success = success;
        this.message = message;
    }

}
