package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.command.DeleteCommand;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.DeleteEvent;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.query.RegistrationNumberQuery;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;

import static org.junit.jupiter.api.Assertions.*;

class DeleteHandlerTest {

    public static final DynamoDBAdapter DB_ADAPTER = DynamoDBAdapter.getInstance();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testDelete() throws RegistrationNumberNotFoundException {

        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(TestHelper.buildRegistrationNumber()),
                DB_ADAPTER.getDynamoDBMapper());
        RegistrationNumber reg = ec.persist();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.DELETE);
        request.setPath(Router.PARENT_PATH);
        request.setBody(JsonUtil.toJson(reg));

        AwsProxyResponse response = new Router().handleRequest(request, null);

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().equals(""));

        // Confirm the reg number and its events have gone

        RegistrationNumberQuery cc = new RegistrationNumberQuery(reg.getMark(), DB_ADAPTER.getDynamoDBMapper());
        assertNull(cc.get());
    }

    @Test
    void testGetFailNoMark() {

        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(TestHelper.buildRegistrationNumber()),
                DB_ADAPTER.getDynamoDBMapper());
        RegistrationNumber reg = ec.persist();

        DeleteCommand ac = new DeleteCommand(new DeleteEvent(reg),
                DB_ADAPTER.getDynamoDBMapper());
        ec.persist();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.POST);
        request.setPath(Router.PARENT_PATH);
        request.setBody(JsonUtil.toJson(reg));

        AwsProxyResponse response = new Router().handleRequest(request, null);

        assertEquals(response.getStatusCode(), 500);
    }
}