package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import javax.ws.rs.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SeedHandlerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    //    @Test
    void handle() {

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.POST);
        request.setPath(Router.SEED_PATH);

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(response.getStatusCode(), 201);
    }

    //    @Test
    void list() {
        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.GET);
        request.setPath(Router.SEED_PATH);

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(response.getStatusCode(), 200);
        System.out.println(response.getBody());
    }
}