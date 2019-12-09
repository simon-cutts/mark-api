package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.EntitlementCreateCommandTest;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.util.DynamoDBAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationNumberQueryHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGet() {

        RegistrationNumber reg = new EntitlementCreateCommandTest().persist();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.REGISTRATION_NUMBER_PATH + reg.getMark());

        RegistrationNumberQueryHandler handler = new RegistrationNumberQueryHandler(DynamoDBAdapter.getInstance());
        AwsProxyResponse response = handler.handle(request);

        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().startsWith("{\"mark\""));
    }

    @Test
    void testGetFailNoMark() {

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.REGISTRATION_NUMBER_PATH);

        RegistrationNumberQueryHandler handler = new RegistrationNumberQueryHandler(DynamoDBAdapter.getInstance());
        AwsProxyResponse response = handler.handle(request);

        assertEquals(response.getStatusCode(), 204);
        assertEquals("",response.getBody());
    }
}