package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.AddressUpdateCommand;
import com.sighware.mark.server.command.Command;
import com.sighware.mark.server.event.AddressUpdatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;

import javax.ws.rs.HttpMethod;

public class AddressHandler extends Handler {

    public AddressHandler(DynamoDBAdapter adapter) {
        super(adapter);
    }

    public AwsProxyResponse handle(AwsProxyRequest request) {

        if (request.getHttpMethod().equals(HttpMethod.POST)) {
            // Get the object from toJson
            RegistrationNumber registrationNumber = JsonUtil.toObject(request.getBody(), RegistrationNumberDocument.class);

            // Create the command with the event
            Command command = new AddressUpdateCommand(
                    new AddressUpdatedEvent(registrationNumber), adapter.getDynamoDBMapper());

            AwsProxyResponse response = getAwsProxyResponse(command, 200);
            return response;
        }
        throw new RuntimeException("Not implemented");
    }
}