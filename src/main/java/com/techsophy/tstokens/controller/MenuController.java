package com.techsophy.tstokens.controller;

import com.techsophy.tstokens.dto.common.ApiResponse;
import com.techsophy.tstokens.dto.common.IApiResponse;
import com.techsophy.tstokens.dto.master.MenuResponsePayload;
import com.techsophy.tstokens.service.MenuService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Validated
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/menus")
@Tag(name = "Menu", description = "The Menu APIs")
public class MenuController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<IApiResponse> getTokenTypeList() {
        logger.info("In getOrganizationList()");
        List<MenuResponsePayload> response = menuService.getApplicationMenu();
        return ResponseEntity.ok()
                .body(new ApiResponse(response, true, "Menu List Fetched Successfully"));
    }
}
