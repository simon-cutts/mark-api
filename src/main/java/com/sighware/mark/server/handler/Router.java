package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.sighware.mark.server.util.DynamoDBAdapter;
import org.apache.log4j.Logger;

public class Router implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    public static final String PARENT_PATH = "/mark/v1";
    public static final String SEED_PATH = PARENT_PATH + "/seed"; // for testing only
    public static final String LOCK_PATH = PARENT_PATH + "/lock";
    public static final String UNLOCK_PATH = PARENT_PATH + "/unlock";
    public static final String ENTITLEMENT_PATH = PARENT_PATH + "/entitlement";
    public static final String ENTITLEMENT_ADDRESS_PATH = PARENT_PATH + "/entitlement/address";
    public static final String REGISTRATION_NUMBER_PATH = PARENT_PATH + "/registrationNumber/";
    public static final String REGISTRATION_NUMBER_EVENT_PATH = PARENT_PATH + "/event/registrationNumber/";

    private static final Logger log = Logger.getLogger(Router.class);
    private static final DynamoDBAdapter DB_ADAPTER = DynamoDBAdapter.getInstance();

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest request, Context context) {

        try {
            log.info(request.getPath());

            if (request.getPath().contains(REGISTRATION_NUMBER_PATH)) {
                return new QueryOrDeleteHandler(DB_ADAPTER).handle(request);

            } else if (request.getPath().contains(REGISTRATION_NUMBER_EVENT_PATH)) {
                return new QueryEventHandler(DB_ADAPTER).handle(request);

            } else {

                switch (request.getPath()) {

                    case SEED_PATH:
                        return new SeedHandler(DB_ADAPTER).handle(request);

                    case ENTITLEMENT_PATH:
                        return new EntitlementHandler(DB_ADAPTER).handle(request);

                    case ENTITLEMENT_ADDRESS_PATH:
                        return new AddressHandler(DB_ADAPTER).handle(request);

                    case LOCK_PATH:
                        return new LockHandler(DB_ADAPTER).handle(request);

                    case UNLOCK_PATH:
                        return new UnLockHandler(DB_ADAPTER).handle(request);

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