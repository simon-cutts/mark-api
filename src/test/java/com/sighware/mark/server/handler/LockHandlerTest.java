package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.query.RegistrationNumberQuery;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;
import com.sighware.mark.server.util.Time;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;

import static org.junit.jupiter.api.Assertions.*;

class LockHandlerTest {

    static DynamoDBAdapter DB_ADAPTER = DynamoDBAdapter.getInstance();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void lockUnLockedRegNum() throws RegistrationNumberNotFoundException {

        RegistrationNumber reg = TestHelper.buildRegistrationNumberSimple();
        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(reg),
                DB_ADAPTER.getDynamoDBMapper());
        reg = ec.persist();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.POST);
        request.setPath(Router.LOCK_PATH);
        request.setBody(JsonUtil.toJson(reg));

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().startsWith("{\"mark\":"));

        RegistrationNumberQuery query = new RegistrationNumberQuery(reg.getMark(), DB_ADAPTER.getDynamoDBMapper());
        reg = query.get();
        assertNotNull(reg.getLockTime());
    }

    @Test
    void lockLockedRegNumFail() throws RegistrationNumberNotFoundException {

        RegistrationNumber reg = TestHelper.buildRegistrationNumberSimple();
        reg.setLockTime(Time.getTimestampNow());
        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(reg),
                DB_ADAPTER.getDynamoDBMapper());
        reg = ec.persist();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.POST);
        request.setPath(Router.LOCK_PATH);
        request.setBody(JsonUtil.toJson(reg));

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(response.getStatusCode(), 409);
        System.out.println(response.getBody());
        assertTrue(response.getBody().startsWith("{\"code\":\"409\""));

        RegistrationNumberQuery query = new RegistrationNumberQuery(reg.getMark(), DB_ADAPTER.getDynamoDBMapper());
        reg = query.get();
        assertNotNull(reg.getLockTime());
    }
}