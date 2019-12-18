package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.util.DynamoDBAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RegistrationNumberLockHandlerTest {

    static DynamoDBAdapter DB_ADAPTER = DynamoDBAdapter.getInstance();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handle() throws IOException {

        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(TestHelper.buildRegistrationNumberSimple()),
                DB_ADAPTER.getDynamoDBMapper());
        RegistrationNumber reg = ec.persist();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.POST);
        request.setPath(Router.REGISTRATION_NUMBER_LOCK_PATH);
        request.setBody(new ObjectMapper().writeValueAsString(reg));

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().startsWith("{\"mark\":"));
    }
}