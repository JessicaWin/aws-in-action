package com.jessica.social.network.serverless.xray.controller;

import com.jessica.social.network.serverless.xray.filter.XRayFilterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对XRayFilter是否开启进行管理
 */
@RestController
@RequestMapping("/xray")
public class XRayController {
    @Autowired
    private XRayFilterConfig filterConfig;

    @RequestMapping(value = "/switch", method = RequestMethod.POST)
    public boolean setEnabled(@RequestParam boolean enabled) {
        this.filterConfig.setFilterEnabled(enabled);
        return true;
    }

    @RequestMapping(value = "/enabled", method = RequestMethod.GET)
    public boolean isEnabled() {
        return this.filterConfig.isFilterEnabled();
    }
}
