package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.util.DynamoDBAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryHandlerTest {

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

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.REGISTRATION_NUMBER_PATH + reg.getMark());

        AwsProxyResponse response = new Router().handleRequest(request, null);


        System.out.println(response.getBody());
        assertTrue(response.getBody().startsWith("{\"mark\""));
    }

    @Test
    void testBuyGet() {

        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(TestHelper.buildRegistrationNumberSimple()),
                DB_ADAPTER.getDynamoDBMapper());
        RegistrationNumber reg = ec.persist();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.REGISTRATION_NUMBER_PATH + reg.getMark());

        AwsProxyResponse response = new Router().handleRequest(request, null);

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().endsWith("\"version\":1}"));
    }

    @Test
    void testGetFailNoMark() {

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.REGISTRATION_NUMBER_PATH);

        AwsProxyResponse response = new Router().handleRequest(request, null);

        assertEquals(response.getStatusCode(), 204);
        assertEquals("", response.getBody());
    }
}