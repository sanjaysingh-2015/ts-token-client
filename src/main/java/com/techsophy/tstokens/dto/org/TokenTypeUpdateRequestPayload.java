package com.techsophy.tstokens.dto.org;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TokenTypeUpdateRequestPayload {
    @NotNull
    @Size(max = 100)
    private String name;
    @Size(max = 10)
    private String tokenPrefix;
    @Size(max = 10)
    private String status;
}
