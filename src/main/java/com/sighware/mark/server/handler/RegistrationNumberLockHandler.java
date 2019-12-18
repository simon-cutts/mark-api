package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.AddressUpdateCommand;
import com.sighware.mark.server.command.Command;
import com.sighware.mark.server.command.RegistrationNumberUpdateCommand;
import com.sighware.mark.server.event.AddressUpdatedEvent;
import com.sighware.mark.server.event.RegistrationNumberLockEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;

import javax.ws.rs.HttpMethod;

public class RegistrationNumberLockHandler extends Handler {

    public RegistrationNumberLockHandler(DynamoDBAdapter adapter) {
        super(adapter);
    }

    public AwsProxyResponse handle(AwsProxyRequest request) {

        if (request.getHttpMethod().equals(HttpMethod.POST)) {
            // Get the object from json
            RegistrationNumber registrationNumber = JsonUtil.toObject(request.getBody(), RegistrationNumberDocument.class);

            // Create the command with the event
            Command command = new RegistrationNumberUpdateCommand(
                    new RegistrationNumberLockEvent(registrationNumber), adapter.getDynamoDBMapper());

            AwsProxyResponse response = getAwsProxyResponse(command, 200);
            return response;
        }
        throw new RuntimeException("Not implemented");
    }
}