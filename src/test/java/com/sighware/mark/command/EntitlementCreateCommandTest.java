package com.sighware.mark.command;

import com.sighware.mark.TestHelper;
import com.sighware.mark.data.DynamoDBAdapter;
import com.sighware.mark.event.EntitlementCreatedEvent;
import com.sighware.mark.model.RegistrationNumberInterface;
import org.junit.jupiter.api.AfterEach;
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
    void persist() {

        RegistrationNumberInterface rn = TestHelper.buildRegistrationNumber();
        EntitlementCreateCommand ec = new EntitlementCreateCommand(new EntitlementCreatedEvent(rn),
                DynamoDBAdapter.getInstance().getDynamoDBMapper());
        ec.persist();
    }
}