package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.Command;
import com.sighware.mark.server.command.UpdateCommand;
import com.sighware.mark.server.event.NomineeUpdateEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;

import javax.ws.rs.HttpMethod;

/**
 * Responsible for updating a nominee of an entitlement
 *
 * @author Simon Cutts
 */
public class NomineeHandler extends Handler {

    public NomineeHandler(DynamoDBAdapter adapter) {
        super(adapter);
    }

    @Override
    public AwsProxyResponse handle(AwsProxyRequest request) {

        // Create entitlement
        if (request.getHttpMethod().equals(HttpMethod.PUT)) {
            // Get the object from toJson
            RegistrationNumber registrationNumber = JsonUtil.toObject(request.getBody(), RegistrationNumberDocument.class);

            // Create the command with the event
            Command command = new UpdateCommand(
                    new NomineeUpdateEvent(registrationNumber), adapter.getDynamoDBMapper());

            return getAwsProxyResponse(command, 200);
        }
        return new AwsProxyResponse(404);
    }
}