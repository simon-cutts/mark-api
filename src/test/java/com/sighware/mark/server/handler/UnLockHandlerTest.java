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

class UnLockHandlerTest {

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
        reg.setLockTime(Time.getTimestamp(Time.getZonedDateTime().minusMinutes(5)));
        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(reg),
                DB_ADAPTER.getDynamoDBMapper());
        reg = ec.persist();
        String mark = reg.getMark();

        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.PUT);
        request.setPath(Router.UNLOCK_PATH + mark);

        AwsProxyResponse response = new Router().handleRequest(request, null);
        assertEquals(response.getStatusCode(), 200);
        assertTrue(response.getBody().startsWith("{\"mark\":"));

        RegistrationNumberQuery query = new RegistrationNumberQuery(reg.getMark(), DB_ADAPTER.getDynamoDBMapper());
        reg = query.get();
        assertNull(reg.getLockTime());
    }
}