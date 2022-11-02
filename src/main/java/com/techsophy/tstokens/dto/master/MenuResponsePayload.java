package com.techsophy.tstokens.dto.master;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponsePayload {
    private String id;
    private String code;
    private String name;
    private String displayName;
    private String icon;
    private String parentMenuCode;
    private String uiPageUrl;
    private Integer displayOrder;
    private String status;
    private List<MenuResponsePayload> childMenu;
}
