package com.jessica.social.network.serverless.xray.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 对XRayFilter的配置进行管理：urlPattern以及filter开启标志位
 */
@Component
public class XRayFilterConfigImpl implements XRayFilterConfig {
    @Value("${xray.url:*}")
    private String url;

    @Value("${xray.filter.enabled:false}")
    private volatile boolean enabled;

    @Override
    public Set<String> getUrlPatterns() {
        String[] filterUrls = this.url.trim().split(",");
        return new HashSet<>(Arrays.asList(filterUrls));
    }

    @Override
    public void setFilterEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isFilterEnabled() {
        return this.enabled;
    }
}
