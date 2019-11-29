package com.sighware.mark.server.command;

import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.data.DynamoDBAdapter;
import com.sighware.mark.server.event.AddressUpdatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddressUpdatedCommandTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void persist() {

        RegistrationNumber rn = TestHelper.buildRegistrationNumber();
        AddressUpdatedCommand ec = new AddressUpdatedCommand(new AddressUpdatedEvent(rn),
                DynamoDBAdapter.getInstance().getDynamoDBMapper());
        ec.persist();
    }
}