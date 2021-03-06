package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.event.EntitlementCreateEvent;
import com.sighware.mark.server.model.RegistrationNumberTable;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;
import com.sighware.mark.server.util.Seeder;

import javax.ws.rs.HttpMethod;

/**
 * Seed handler for creating test data
 */
public class SeedHandler extends Handler {

    public SeedHandler(DynamoDBAdapter adapter) {
        super(adapter);
    }

    @Override
    public AwsProxyResponse handle(AwsProxyRequest request) {

        if (request.getHttpMethod().equals(HttpMethod.POST)) {

            for (int i = 0; i <= 50; i++) {
                EntitlementCreateCommand ec = new EntitlementCreateCommand(
                        new EntitlementCreateEvent(Seeder.buildRegistrationNumberSimple()),
                        adapter.getDynamoDBMapper());
                ec.persist();
            }

            AwsProxyResponse response = new AwsProxyResponse();
            response.setStatusCode(201);
            return response;

        }
        return new AwsProxyResponse(404);
    }
}