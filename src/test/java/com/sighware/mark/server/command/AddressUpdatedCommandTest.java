package com.sighware.mark.server.command;

import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.event.AddressUpdatedEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.util.DynamoDBAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddressUpdatedCommandTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void persist() {

        RegistrationNumber rn = TestHelper.buildRegistrationNumber();
        AddressUpdateCommand ec = new AddressUpdateCommand(new AddressUpdatedEvent(rn),
                DynamoDBAdapter.getInstance().getDynamoDBMapper());
        ec.persist();
    }
}