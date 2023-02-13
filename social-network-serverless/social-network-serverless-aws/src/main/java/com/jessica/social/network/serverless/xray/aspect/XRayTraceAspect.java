package com.jessica.social.network.serverless.xray.aspect;

import com.amazonaws.xray.AWSXRay;
import com.amazonaws.xray.entities.Segment;
import com.amazonaws.xray.entities.Subsegment;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * XRayTraceAspect的主要作用是创建XRay的子分片
 */
@Aspect
@Component
@Qualifier("XRayTraceAspect")
public class XRayTraceAspect {
    public Object traceAround(ProceedingJoinPoint jp) throws Throwable {
        /**
         * 只要调用的方法符合pointcut指定的JoinPoint的规则，XRayTraceAspect的traceAround方法就会执行，
         * 不管调用该JoinPoint对应的requst的url是否符合XRayilter的urlPattern或者XRayilter是否开启
         * 但是主分片只有在XRayilter被应用，即XRayilter开启且requst的url符合XRayilter的urlPattern时才会创建
         * 因此可以利用主分片是否存在来作为是否需要创建子分片的条件
         */
        Optional<Segment> segment = AWSXRay.getCurrentSegmentOptional();
        if (segment.isPresent()) {
            // 主分片存在，则创建子分片
            MethodSignature methodSignature = (MethodSignature) jp.getSignature();
            String className = methodSignature.getDeclaringType().getName();
            // 创建子分片，并设置子分片的名字为simpleClassName.methodName，此时开始计时
            Subsegment subsegment = AWSXRay.beginSubsegment(methodSignature.getDeclaringType().getSimpleName() + "." + methodSignature.getName());
            // 设置子分片的metadata为className
            subsegment.putMetadata("className", className);
            Object result = null;
            try {
                // 执行JoinPoint
                result = jp.proceed(jp.getArgs());
            } catch (Throwable throwable) {
                // 向子分片添加异常信息
                subsegment.addException(throwable);
                throw new RuntimeException(throwable);
            } finally {
                // 结束计时
                AWSXRay.endSubsegment();
            }
            return result;
        } else {
            // 主分片不存在直接执行JoinPoint
            return jp.proceed(jp.getArgs());
        }
    }
}
