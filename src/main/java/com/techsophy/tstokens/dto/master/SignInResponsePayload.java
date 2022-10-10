package com.techsophy.tstokens.dto.master;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SignInResponsePayload {
    private String accessToken;
    private String tokenType;
    private String refreshToken;
    private String expiresIn;
    private String scope;
    private String expiresTime;
    private Integer index;
    private Integer countryId;
    private String countryCode;
    private String countryName;
    private String dateFormat;
}
