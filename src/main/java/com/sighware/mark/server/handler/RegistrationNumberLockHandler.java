package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.RegistrationNumberLockCommand;
import com.sighware.mark.server.error.LockFailedException;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.RegistrationNumberLockEvent;
import com.sighware.mark.server.model.Error;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;

import javax.ws.rs.HttpMethod;

import static com.sighware.mark.server.util.JsonUtil.toJson;

public class RegistrationNumberLockHandler extends Handler {

    public RegistrationNumberLockHandler(DynamoDBAdapter adapter) {
        super(adapter);
    }

    public AwsProxyResponse handle(AwsProxyRequest request) {

        if (request.getHttpMethod().equals(HttpMethod.POST)) {
            // Get the object from json
            RegistrationNumber registrationNumber = JsonUtil.toObject(request.getBody(), RegistrationNumberDocument.class);

            // Create the command with the event
            RegistrationNumberLockCommand command = new RegistrationNumberLockCommand(
                    new RegistrationNumberLockEvent(registrationNumber), adapter.getDynamoDBMapper());

            try {
                command.process();
            } catch (RegistrationNumberNotFoundException | LockFailedException e) {
                response.setStatusCode(409);
                response.setBody(toJson(new Error("409", "Conflict", e.getMessage())));
                return response;
            }
            return getAwsProxyResponse(command, 200);
        }
        throw new RuntimeException("Not implemented");
    }
}