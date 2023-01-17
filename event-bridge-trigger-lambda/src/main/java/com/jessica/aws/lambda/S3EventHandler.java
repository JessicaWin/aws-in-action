package com.jessica.aws.lambda;

import com.alibaba.fastjson.JSON;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class S3EventHandler implements RequestHandler<Object, Boolean> {
    @Override
    public Boolean handleRequest(Object s3Event, Context context) {
        log.info("Received event data: " + s3Event);
        String jsonString = JSON.toJSONString(s3Event);
        S3EventBridgeObject event = JSON.parseObject(jsonString, S3EventBridgeObject.class);
        log.info(String.format("S3event: event type: %s, bucket name:%s, file key:%s", event.getDetail().getBucket().getName(), event.getDetailType(),
                event.getDetail().getObject().getKey()));
        return true;
    }
}
