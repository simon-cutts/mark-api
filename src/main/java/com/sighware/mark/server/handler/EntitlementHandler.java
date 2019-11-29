package com.sighware.mark.server.handler;

import com.amazonaws.HttpMethod;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.data.DynamoDBAdapter;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.model.Error;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import org.apache.log4j.Logger;

import java.io.IOException;

public class EntitlementHandler {
    private static final Logger log = Logger.getLogger(EntitlementHandler.class);

    private ObjectMapper objectMapper = new ObjectMapper();
    private DynamoDBAdapter adapter;

    public EntitlementHandler(DynamoDBAdapter adapter) {
        this.adapter = adapter;
    }

    public AwsProxyResponse handle(AwsProxyRequest request) throws IOException {
        log.info("handle");

        if (request.getHttpMethod().equals(HttpMethod.POST.name())) {
            // Get the object from json
            RegistrationNumber registrationNumber = objectMapper.readValue(request.getBody(), RegistrationNumberDocument.class);

            // Create the command with the event
            EntitlementCreateCommand command = new EntitlementCreateCommand(
                    new EntitlementCreatedEvent(registrationNumber), adapter.getDynamoDBMapper());

            String json = null;
            AwsProxyResponse response = new AwsProxyResponse();
            response.addHeader("Content-Type", "application/json");
            try {
                json = objectMapper.writeValueAsString(command.persist());
                response.setStatusCode(201);
            } catch (Exception e) {
                log.error(e);
                response.setStatusCode(500);
                Error err = new Error("500", "Internal Server Error", "Internal Server Error");
                json = err.toJson();
            }
            response.setBody(json);
            return response;
        }
        throw new RuntimeException("Not implemented");
    }
}