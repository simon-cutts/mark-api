package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.UnLockCommand;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.UnLockEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.query.RegistrationNumberQuery;
import com.sighware.mark.server.util.DynamoDBAdapter;

import javax.ws.rs.HttpMethod;

public class UnLockHandler extends Handler {

    public UnLockHandler(DynamoDBAdapter adapter) {
        super(adapter);
    }

    @Override
    public AwsProxyResponse handle(AwsProxyRequest request) {

        if (request.getHttpMethod().equals(HttpMethod.PUT)) {

            // Get the object from json
            String mark = Path.getRegistrationNumber(request.getPath());
            try {
                RegistrationNumberQuery query = new RegistrationNumberQuery(mark, adapter.getDynamoDBMapper());
                RegistrationNumber registrationNumber = query.get();

                if (registrationNumber == null) throw new RegistrationNumberNotFoundException();

                // Create the command with the event
                UnLockCommand command = new UnLockCommand(
                        new UnLockEvent(registrationNumber), adapter.getDynamoDBMapper());
                command.process();
                return getAwsProxyResponse(command, 200);

            } catch (RegistrationNumberNotFoundException e) {
                response.setStatusCode(404);
                log.info("Unable to find mark " + mark);

            }
            return response;
        }
        return new AwsProxyResponse(404);
    }
}