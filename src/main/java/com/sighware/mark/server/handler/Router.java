package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sighware.mark.server.util.DynamoDBAdapter;
import org.apache.log4j.Logger;

public class Router implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    public static final String REGISTRATION_NUMBER_PATH = "/mark/v1/registrationNumber/";
    public static final String ENTITLEMENT_PATH = "/mark/v1/entitlement";
    public static final String ENTITLEMENT_ADDRESS_PATH = "/mark/v1/entitlement/address";

    private static final Logger log = Logger.getLogger(Router.class);

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest request, Context context) {

        try {
            log.info(request.getPath());

            if (request.getPath().contains(REGISTRATION_NUMBER_PATH)) {
                return new RegistrationNumberQueryHandler(DynamoDBAdapter.getInstance()).handle(request);

            } else {

                switch (request.getPath()) {

                    case ENTITLEMENT_PATH:
                        return new EntitlementHandler(DynamoDBAdapter.getInstance()).handle(request);

                    case ENTITLEMENT_ADDRESS_PATH:
                        return new AddressHandler(DynamoDBAdapter.getInstance()).handle(request);

                    default:
                        return new AwsProxyResponse(404);
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return new AwsProxyResponse(500);
        }
    }
}