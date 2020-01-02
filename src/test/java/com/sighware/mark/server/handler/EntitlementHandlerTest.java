package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.event.EntitlementCreateEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.Seeder;
import com.sighware.mark.server.util.Time;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EntitlementHandlerTest {

    static DynamoDBAdapter DB_ADAPTER = DynamoDBAdapter.getInstance();

    RegistrationNumber reg;
    AwsProxyRequest request;

    @BeforeEach
    void setUp() {
        reg = Seeder.buildRegistrationNumber();
        request = new AwsProxyRequest();
        request.setPath(Router.ENTITLEMENT_PATH);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createEntitlement() throws IOException {

        request.setHttpMethod(HttpMethod.POST);
        request.setBody(new ObjectMapper().writeValueAsString(reg));

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(response.getStatusCode(), 201);
        assertTrue(response.getBody().startsWith("{\"mark\":"));
    }

    @Test
    void extendEntitlement() throws IOException {

        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreateEvent(reg),
                DB_ADAPTER.getDynamoDBMapper());
        reg = ec.persist();

        String now = Time.getTimestamp();
        reg.getEntitlement().setAgreementTime(now);
        request.setHttpMethod(HttpMethod.PUT);
        request.setBody(new ObjectMapper().writeValueAsString(reg));

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().contains(now));
    }
}