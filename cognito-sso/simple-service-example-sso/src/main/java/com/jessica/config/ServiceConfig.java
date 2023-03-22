package com.jessica.config;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class ServiceConfig {
    @Bean
    public RestTemplate geRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        List<CaffeineCache> caches = new ArrayList<>();
        caches.add(new CaffeineCache("CognitoJwksCache",
                Caffeine.newBuilder()
                        .expireAfterWrite(900, TimeUnit.SECONDS)
                        .build()));

        cacheManager.setCaches(caches);
        return cacheManager;
    }
}
