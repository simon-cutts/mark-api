package com.sighware.mark.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.data.DynamoDBAdapter;
import org.apache.log4j.Logger;

public class Router implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    private static final Logger log = Logger.getLogger(Router.class);

    private ObjectMapper objectMapper;

    public Router() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest request, Context context) {

        try {
            log.info(request.getPath());
            AwsProxyResponse response = null;
            switch (request.getPath()) {
                case "/som/v1/entitlement":
                    log.info("case /som/v1/entitlement");

                    EntitlementHandler handler = new EntitlementHandler(DynamoDBAdapter.getInstance());

                    response = new AwsProxyResponse(200);
                    response.addHeader("Content-Type", "application/json");

                    response.setBody(objectMapper.writeValueAsString(handler.handle(request)));

                    return response;

//                case "/command/redeem":
//                    shop.redeem(objectMapper.readValue(request.getBody(), RedeemCmd.class));
//                    return new AwsProxyResponse(204);
                default:
                    return new AwsProxyResponse(404);
            }
        } catch (Exception ex) {
            log.error(ex);
            return new AwsProxyResponse(500);
        }

    }

}