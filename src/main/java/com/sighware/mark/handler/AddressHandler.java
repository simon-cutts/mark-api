package com.sighware.mark.handler;

import com.amazonaws.HttpMethod;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.command.AddressUpdatedCommand;
import com.sighware.mark.data.DynamoDBAdapter;
import com.sighware.mark.event.AddressUpdatedEvent;
import com.sighware.mark.model.Error;
import com.sighware.mark.model.RegistrationNumber;
import org.apache.log4j.Logger;

import java.io.IOException;

public class AddressHandler {
    private static final Logger log = Logger.getLogger(AddressHandler.class);

    private ObjectMapper objectMapper = new ObjectMapper();
    private DynamoDBAdapter adapter;

    public AddressHandler(DynamoDBAdapter adapter) {
        this.adapter = adapter;
    }

    public AwsProxyResponse handle(AwsProxyRequest request) throws IOException {
        log.info("handle");

        // Get the object from json
        RegistrationNumber registrationNumber = objectMapper.readValue(request.getBody(), RegistrationNumber.class);

        if (request.getHttpMethod().equals(HttpMethod.POST.name())) {

            // Create the command with the event
            AddressUpdatedCommand command = new AddressUpdatedCommand(
                    new AddressUpdatedEvent(registrationNumber), adapter.getDynamoDBMapper());

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