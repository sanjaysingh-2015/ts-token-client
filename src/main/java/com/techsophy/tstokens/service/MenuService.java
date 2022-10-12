package com.techsophy.tstokens.service;

import com.techsophy.tstokens.dto.master.MenuResponsePayload;
import com.techsophy.tstokens.entity.Menu;
import com.techsophy.tstokens.repository.MenuRepository;
import com.techsophy.tstokens.utils.ApplicationMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MenuRepository menuRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuResponsePayload> getApplicationMenu() {
        logger.info("In getDepartmentDetails()");
        List<Menu> menuList = menuRepository.findByStatus("ACTIVE");
        List<MenuResponsePayload> response = new ArrayList<>();
        ApplicationMapping<MenuResponsePayload, Menu> responseMapping = new ApplicationMapping<>();
        for (Menu menu : menuList) {
            response.add(responseMapping.convert(menu, MenuResponsePayload.class));
        }
        return response;
    }
}
