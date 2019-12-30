package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.UnLockCommand;
import com.sighware.mark.server.event.LockEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;

import javax.ws.rs.HttpMethod;

public class UnLockHandler extends Handler {

    public UnLockHandler(DynamoDBAdapter adapter) {
        super(adapter);
    }

    public AwsProxyResponse handle(AwsProxyRequest request) {

        if (request.getHttpMethod().equals(HttpMethod.POST)) {
            // Get the object from json
            RegistrationNumber registrationNumber = JsonUtil.toObject(request.getBody(), RegistrationNumberDocument.class);

            // Create the command with the event
            UnLockCommand command = new UnLockCommand(
                    new LockEvent(registrationNumber), adapter.getDynamoDBMapper());

            command.process();
            return getAwsProxyResponse(command, 200);
        }
        throw new RuntimeException("Not implemented");
    }
}