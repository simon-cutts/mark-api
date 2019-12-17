package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.model.RegistrationNumber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntitlementHandlerTest {

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
        request.setPath(Router.ENTITLEMENT_PATH);
        request.setBody(new ObjectMapper().writeValueAsString(reg));

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(response.getStatusCode(), 201);
        assertTrue(response.getBody().startsWith("{\"mark\":"));
    }
}