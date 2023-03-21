package com.jessica.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.jessica.security.config.CacheConfig;
import com.jessica.security.config.RestTemplateConfig;
import com.jessica.security.config.SSOAuthFilterConfig;

@Configuration
@Import({ RestTemplateConfig.class, SSOAuthFilterConfig.class, CacheConfig.class })
public class ServiceConfig {
}
