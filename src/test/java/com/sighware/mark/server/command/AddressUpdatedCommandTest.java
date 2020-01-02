package com.sighware.mark.server.command;

import com.sighware.mark.server.event.AddressUpdateEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.Seeder;
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
    public void persist() {

        RegistrationNumber rn = Seeder.buildRegistrationNumber();
        UpdateCommand ec = new UpdateCommand(new AddressUpdateEvent(rn),
                DynamoDBAdapter.getInstance().getDynamoDBMapper());
        ec.persist();
    }
}