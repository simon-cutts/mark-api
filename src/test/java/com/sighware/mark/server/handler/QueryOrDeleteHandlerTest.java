package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.event.EntitlementCreateEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.Seeder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueryOrDeleteHandlerTest {

    public static final DynamoDBAdapter DB_ADAPTER = DynamoDBAdapter.getInstance();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGet() {
        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreateEvent(Seeder.buildRegistrationNumber()),
                DB_ADAPTER.getDynamoDBMapper());
        RegistrationNumber reg = ec.persist();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.REGISTRATION_NUMBER_PATH + reg.getMark());

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().startsWith("{\"mark\""));
    }

    @Test
    void testBuyGet() {
        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreateEvent(Seeder.buildRegistrationNumberSimple()),
                DB_ADAPTER.getDynamoDBMapper());
        RegistrationNumber reg = ec.persist();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.REGISTRATION_NUMBER_PATH + reg.getMark());

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().endsWith("\"version\":1}"));
    }

    @Test
    void testGetFailNoMark() {
        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.REGISTRATION_NUMBER_PATH);

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    void testMarkNotFound() {
        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.REGISTRATION_NUMBER_PATH + "NOT_EXIST");

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(404, response.getStatusCode());
    }
}