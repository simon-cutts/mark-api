package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sighware.mark.server.data.DynamoDBAdapter;
import org.apache.log4j.Logger;

public class Router implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private static final Logger log = Logger.getLogger(Router.class);

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest request, Context context) {

        try {
            log.info(request.getPath());
            switch (request.getPath()) {
                case "/mark/v1/entitlement":
                    return new EntitlementHandler(DynamoDBAdapter.getInstance()).handle(request);

                case "/mark/v1/entitlement/address":
                    return new AddressHandler(DynamoDBAdapter.getInstance()).handle(request);

                default:
                    return new AwsProxyResponse(404);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AwsProxyResponse(500);
        }
    }
}