package com.jessica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.jessica.security.config.CacheConfig;
import com.jessica.security.config.RestTemplateConfig;
import com.jessica.security.config.ServiceAuthFilterConfig;

@Configuration
@Import({ RestTemplateConfig.class, ServiceAuthFilterConfig.class, CacheConfig.class })
public class ServiceConfig {
}
