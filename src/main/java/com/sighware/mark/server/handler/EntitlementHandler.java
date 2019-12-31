package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.Command;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.error.ResourceNotFoundException;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;

import javax.ws.rs.HttpMethod;

public class EntitlementHandler extends Handler {

    public EntitlementHandler(DynamoDBAdapter adapter) {
        super(adapter);
    }

    @Override
    public AwsProxyResponse handle(AwsProxyRequest request) throws ResourceNotFoundException {

        if (request.getHttpMethod().equals(HttpMethod.POST)) {
            // Get the object from toJson
            RegistrationNumber registrationNumber = JsonUtil.toObject(request.getBody(), RegistrationNumberDocument.class);

            // Create the command with the event
            Command command = new EntitlementCreateCommand(
                    new EntitlementCreatedEvent(registrationNumber), adapter.getDynamoDBMapper());

            AwsProxyResponse response = getAwsProxyResponse(command, 201);
            return response;
        }
        throw new ResourceNotFoundException();
    }
}