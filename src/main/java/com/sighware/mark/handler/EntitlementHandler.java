package com.sighware.mark.handler;

import com.amazonaws.HttpMethod;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.command.EntitlementCreateCommand;
import com.sighware.mark.data.DynamoDBAdapter;
import com.sighware.mark.event.EntitlementCreatedEvent;
import com.sighware.mark.model.RegistrationNumber;
import com.sighware.mark.model.RegistrationNumberInterface;
import org.apache.log4j.Logger;

import java.io.IOException;

public class EntitlementHandler {

    private static final Logger log = Logger.getLogger(EntitlementHandler.class);

    private ObjectMapper objectMapper = new ObjectMapper();
    private DynamoDBAdapter adapter;

    public EntitlementHandler(DynamoDBAdapter adapter) {
        this.adapter = adapter;
    }

    public RegistrationNumberInterface handle(AwsProxyRequest request) throws IOException {

        log.info("handle");

        RegistrationNumber rg = objectMapper.readValue(request.getBody(), RegistrationNumber.class);

        if (request.getHttpMethod().equals(HttpMethod.POST.name())) {
            EntitlementCreateCommand ec = new EntitlementCreateCommand(
                    new EntitlementCreatedEvent(rg), adapter.getDynamoDBMapper());
            return ec.persist();
        }
        return null;
    }
}
