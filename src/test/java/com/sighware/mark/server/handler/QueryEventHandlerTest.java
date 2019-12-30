package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.command.UpdateCommand;
import com.sighware.mark.server.event.AddressUpdatedEvent;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.util.DynamoDBAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryEventHandlerTest {

    public static final DynamoDBAdapter DB_ADAPTER = DynamoDBAdapter.getInstance();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGet() {

        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(TestHelper.buildRegistrationNumber()),
                DB_ADAPTER.getDynamoDBMapper());
        RegistrationNumber reg = ec.persist();

        UpdateCommand ac = new UpdateCommand(new AddressUpdatedEvent(reg),
                DB_ADAPTER.getDynamoDBMapper());
        ec.persist();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.REGISTRATION_NUMBER_EVENT_PATH + reg.getMark());

        AwsProxyResponse response = new Router().handleRequest(request, null);

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().startsWith("{\"events\":[{\"createTime\""));
    }

    @Test
    void testGetFailNoMark() {

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.REGISTRATION_NUMBER_EVENT_PATH);

        AwsProxyResponse response = new Router().handleRequest(request, null);

        assertEquals(response.getStatusCode(), 204);
        assertEquals("", response.getBody());
    }
}