package com.jessica.aws.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LambdaProxyHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(LambdaProxyHandler.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        GreetingRequestVo greetingRequestVo = null;
        try {
            LOGGER.info("api gateway event is: " + event);
            String requestBody = event.getBody();
            LOGGER.info("api requestBody: " + event);
            greetingRequestVo = mapper.readValue(requestBody, GreetingRequestVo.class);
            String greeting = String.format("Good %s, %s of %s.[ Happy %s!]",
                    greetingRequestVo.getTime(), greetingRequestVo.getName(), greetingRequestVo.getCity(), greetingRequestVo.getDay());
            LOGGER.info("name is " + greetingRequestVo.getName());
            GreetingResponseVo response = GreetingResponseVo.builder().greeting(greeting).build();
            String responseBody = mapper.writeValueAsString(response);
            APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
            responseEvent.setBody(responseBody);
            responseEvent.setStatusCode(200);
            return responseEvent;
        } catch (JsonProcessingException e) {
            APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
            responseEvent.setStatusCode(500);
            return responseEvent;
        }
    }
}
