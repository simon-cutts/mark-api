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
import com.sighware.mark.server.util.JsonUtil;
import com.sighware.mark.server.util.Seeder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.ws.rs.HttpMethod;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

class AddressHandlerTest {

    public static final DynamoDBAdapter DB_ADAPTER = DynamoDBAdapter.getInstance();

    EntitlementCreateCommand ec;
    RegistrationNumber reg;
    String mark;

    @BeforeEach
    void setUp() {
        ec = new EntitlementCreateCommand(new EntitlementCreateEvent(Seeder.buildRegistrationNumber()),
                DB_ADAPTER.getDynamoDBMapper());
        reg = ec.persist();
        mark = reg.getMark();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testChangeAddress() {
        AwsProxyRequest request = new AwsProxyRequest();
        request.setHttpMethod(HttpMethod.PUT);
        request.setPath(Router.ENTITLEMENT_ADDRESS_PATH);
        request.setBody(JsonUtil.toJson(reg));

        AwsProxyResponse response = new Router().handleRequest(request, null);

        assertEquals(response.getStatusCode(), 200);
//        System.out.println(response.getBody());
        assertTrue(response.getBody().contains("version\":2"));
    }

}