package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.EntitlementCreateEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.query.EventQuery;
import com.sighware.mark.server.query.RegistrationNumberQuery;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.Seeder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DeleteHandlerTest {

    public static final DynamoDBAdapter DB_ADAPTER = DynamoDBAdapter.getInstance();

    EntitlementCreateCommand ec;
    RegistrationNumber reg;
    String mark;

    @BeforeEach
    void setUp() {
        ec = new EntitlementCreateCommand(new EntitlementCreateEvent(Seeder.buildRegistrationNumber()),
                DB_ADAPTER.getDynamoDBMapper());
        RegistrationNumber reg = ec.persist();
        mark = reg.getMark();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testDelete() throws RegistrationNumberNotFoundException {
        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.DELETE);
        request.setPath(Router.REGISTRATION_NUMBER_PATH + "/" + mark);

        AwsProxyResponse response = new Router().handleRequest(request, null);

        assertEquals(response.getStatusCode(), 204);
        assertNull(response.getBody());

        // Confirm the reg number and its events have gone
        RegistrationNumberQuery cc = new RegistrationNumberQuery(reg.getMark(), DB_ADAPTER.getDynamoDBMapper());
        assertNull(cc.get());

        EventQuery query = new EventQuery(DB_ADAPTER.getDynamoDBMapper(), mark);
        assertEquals(0, query.get().getEvents().size());
    }

    @Test
    void testGetFailNoMark() {
        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.POST);
        request.setPath(Router.REGISTRATION_NUMBER_PATH + "/" + mark);

        AwsProxyResponse response = new Router().handleRequest(request, null);

        assertEquals(response.getStatusCode(), 404);
    }
}