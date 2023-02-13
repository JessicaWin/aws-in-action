package com.jessica.social.network.serverless.config;

import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.jessica.social.network.serverless.xray.filter.XRayFilter;
import com.jessica.social.network.serverless.xray.filter.XRayFilterConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class XRayConfig {

    @Autowired
    private XRayFilterConfig filterConfig;

//    @Bean
//    public XRayFilter xRayFilter() {
//        return new XRayFilter("social-network-serverless-framework", this.filterConfig);
//    }

    // 不直接声明XRayFilter的bean，而是使用FilterRegistrationBean对XRayFilter的bran进行注册，是为了给XRayFilter设置urlPattern
    @Bean
    public FilterRegistrationBean<AWSXRayServletFilter> xRayFilter() {
        FilterRegistrationBean<AWSXRayServletFilter> registrationBean
                = new FilterRegistrationBean<>();
        XRayFilter filter = new XRayFilter("social-network-serverless-framework", this.filterConfig);
        registrationBean.setFilter(filter);
        this.filterConfig.getUrlPatterns().forEach(url -> registrationBean.addUrlPatterns(url));
        return registrationBean;
    }
}
