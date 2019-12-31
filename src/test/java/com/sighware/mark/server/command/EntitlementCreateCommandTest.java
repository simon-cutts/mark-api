package com.sighware.mark.server.command;

import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.query.RegistrationNumberQuery;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;
import com.sighware.mark.server.util.Seeder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntitlementCreateCommandTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void persist() throws RegistrationNumberNotFoundException {

        RegistrationNumber rn = Seeder.buildRegistrationNumber();
        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(rn),
                DynamoDBAdapter.getInstance().getDynamoDBMapper());
        ec.persist();

        System.out.println(JsonUtil.toJson(rn));
        Assertions.assertNotNull(new RegistrationNumberQuery(rn.getMark(),
                DynamoDBAdapter.getInstance().getDynamoDBMapper()).get());
    }
}