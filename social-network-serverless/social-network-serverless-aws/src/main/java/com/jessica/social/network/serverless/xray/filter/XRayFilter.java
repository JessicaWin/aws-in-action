package com.jessica.social.network.serverless.xray.filter;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.AWSXRayRecorderBuilder;
import com.amazonaws.xray.javax.servlet.AWSXRayServletFilter;
import com.amazonaws.xray.strategy.sampling.CentralizedSamplingStrategy;
import com.jessica.social.network.serverless.config.XRayConfig;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.net.URL;

/**
 * 1. 使用XRayFilter来对AWSXRayServletFilter进行功能增强
 * AWSXRayServletFilter的基本作用是对请求进行过滤，创建XRay的主分片，并在responseHeader中添加名为X-Amzn-Trace-Id的header
 * XRayFilter的增强在于只有在filterConfig的filterEnable标志为true时才会应用，为false时直接跳过当前filter
 * 2. XRayFilter并没有添加Component注解，一是因为继承自AWSXRayServletFilter，构造时必须提供有一个string或者SegmentNamingStrategy类型的构造参数，需要为该参数创建一个bean才能进行自动依赖注入，
 * 二是为了使用FilterRegistrationBean对XRayFilter进行bean注册
 */
public class XRayFilter extends AWSXRayServletFilter {
    /*
     因为XRayFilter没有添加Component注解，因此不能使用Autowired对filterConfig进行注入，
     声明为final采用强制以构造函数参数的方式进行初始化
     */
    private final XRayFilterConfig filterConfig;

    public XRayFilter(String segmentName, XRayFilterConfig filterConfig) {
        super(segmentName);
        this.filterConfig = filterConfig;
    }

    static {
        AWSXRayRecorderBuilder builder = AWSXRayRecorderBuilder.standard();
        URL ruleFile = XRayConfig.class.getClassLoader().getResource("xray-rules.json");
        builder.withSamplingStrategy(new CentralizedSamplingStrategy(ruleFile));
        AWSXRay.setGlobalRecorder(builder.build());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (this.filterConfig != null && this.filterConfig.isFilterEnabled()) {
            super.doFilter(request, response, chain);
        } else {
            chain.doFilter(request, response);
        }
    }
}
