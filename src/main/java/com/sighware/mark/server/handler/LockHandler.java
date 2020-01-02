package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.LockCommand;
import com.sighware.mark.server.error.LockFailedException;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.LockEvent;
import com.sighware.mark.server.model.Error;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.query.RegistrationNumberQuery;
import com.sighware.mark.server.util.DynamoDBAdapter;

import javax.ws.rs.HttpMethod;

import static com.sighware.mark.server.util.JsonUtil.toJson;

public class LockHandler extends Handler {

    public LockHandler(DynamoDBAdapter adapter) {
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
                LockCommand command = new LockCommand(
                        new LockEvent(registrationNumber), adapter.getDynamoDBMapper());
                command.process();
                return getAwsProxyResponse(command, 200);

            } catch (RegistrationNumberNotFoundException e) {
                response.setStatusCode(404);
                log.info("Unable to find mark " + mark);

            } catch (LockFailedException e) {
                response.setStatusCode(409);
                response.setBody(toJson(new Error("409", "Conflict", e.getMessage())));
            }
            return response;
        }
        return new AwsProxyResponse(404);
    }
}