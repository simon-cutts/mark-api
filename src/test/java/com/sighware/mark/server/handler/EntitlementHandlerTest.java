package com.sighware.mark.server.handler;

import com.amazonaws.HttpMethod;
import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.data.DynamoDBAdapter;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.model.Error;
import com.sighware.mark.server.model.RegistrationNumber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EntitlementHandlerTest {
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handle() throws IOException {

        RegistrationNumber reg = TestHelper.buildRegistrationNumber();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod("POST");
        request.setBody(new ObjectMapper().writeValueAsString(reg));

        EntitlementHandler handler = new EntitlementHandler(DynamoDBAdapter.getInstance());
        handler.handle(request);
    }
}