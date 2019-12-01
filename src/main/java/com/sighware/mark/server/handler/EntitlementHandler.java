package com.sighware.mark.server.handler;

import com.amazonaws.HttpMethod;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.Command;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.data.DynamoDBAdapter;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberDocument;

import java.io.IOException;

public class EntitlementHandler extends Handler {

    public EntitlementHandler(DynamoDBAdapter adapter) {
        super(adapter);
    }

    public AwsProxyResponse handle(AwsProxyRequest request) throws IOException {
        log.info("EntitlementHandler");

        if (request.getHttpMethod().equals(HttpMethod.POST.name())) {
            // Get the object from json
            RegistrationNumber registrationNumber = objectMapper.readValue(request.getBody(), RegistrationNumberDocument.class);

            // Create the command with the event
            Command command = new EntitlementCreateCommand(
                    new EntitlementCreatedEvent(registrationNumber), adapter.getDynamoDBMapper());

            AwsProxyResponse response = getAwsProxyResponse(command, 201);
            return response;
        }
        throw new RuntimeException("Not implemented");
    }
}