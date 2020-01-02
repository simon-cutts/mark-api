package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.query.RegistrationNumberQuery;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.Seeder;
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
    void unLockRegNum() throws RegistrationNumberNotFoundException {

        RegistrationNumber reg = Seeder.buildRegistrationNumberSimple();
        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(reg),
                DB_ADAPTER.getDynamoDBMapper());
        reg = ec.persist();
        String mark = reg.getMark();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.PUT);
        request.setPath(Router.LOCK_PATH + mark);

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().startsWith("{\"mark\":"));

        RegistrationNumberQuery query = new RegistrationNumberQuery(reg.getMark(), DB_ADAPTER.getDynamoDBMapper());
        reg = query.get();
        assertNotNull(reg.getLockTime());
    }

    @Test
    void unLockRegNumForExpiredLock() throws RegistrationNumberNotFoundException {

        RegistrationNumber reg = Seeder.buildRegistrationNumberSimple();
        reg.setLockTime(Time.getTimestamp(Time.getZonedDateTime().minusMinutes(11)));
        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(reg),
                DB_ADAPTER.getDynamoDBMapper());

        // Set lock time as expired 11 minutes ago
        reg = ec.persist();
        String mark = reg.getMark();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.PUT);
        request.setPath(Router.LOCK_PATH + mark);

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(200, response.getStatusCode());
        assertTrue(response.getBody().startsWith("{\"mark\":"));

        RegistrationNumberQuery query = new RegistrationNumberQuery(reg.getMark(), DB_ADAPTER.getDynamoDBMapper());
        reg = query.get();
        assertNotNull(reg.getLockTime());
    }

    @Test
    void unLockRegNumFailMarkNotAvailable() {

        RegistrationNumber reg = Seeder.buildRegistrationNumber();
        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(reg),
                DB_ADAPTER.getDynamoDBMapper());
        reg = ec.persist();
        String mark = reg.getMark();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.PUT);
        request.setPath(Router.LOCK_PATH + mark);

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(409, response.getStatusCode());
        assertTrue(response.getBody().startsWith("{\"code\":\"409\""));
    }

    @Test
    void lockedRegNumFail() throws RegistrationNumberNotFoundException {

        RegistrationNumber reg = Seeder.buildRegistrationNumberSimple();
        reg.setLockTime(Time.getTimestampPlus10Min());
        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(reg),
                DB_ADAPTER.getDynamoDBMapper());
        reg = ec.persist();
        String mark = reg.getMark();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.PUT);
        request.setPath(Router.LOCK_PATH + mark);

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(409, response.getStatusCode());
        assertTrue(response.getBody().startsWith("{\"code\":\"409\""));

        RegistrationNumberQuery query = new RegistrationNumberQuery(reg.getMark(), DB_ADAPTER.getDynamoDBMapper());
        reg = query.get();
        assertNotNull(reg.getLockTime());
    }

    @Test
    void lockedRegNumFailMarkNotExist() throws RegistrationNumberNotFoundException {

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.PUT);
        request.setPath(Router.LOCK_PATH + "NOT_EXIST");

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(404, response.getStatusCode());
    }
}