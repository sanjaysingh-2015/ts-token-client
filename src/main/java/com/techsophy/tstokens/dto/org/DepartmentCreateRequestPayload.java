package com.techsophy.tstokens.dto.org;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentCreateRequestPayload {
    List<TokenCategoryCreateRequestPayload> tokenCategories;
    @NotNull
    @Size(max = 10)
    private String code;
    @NotNull
    @Size(max = 100)
    private String name;
    @Size(max = 10)
    private String tokenPrefix;
}
