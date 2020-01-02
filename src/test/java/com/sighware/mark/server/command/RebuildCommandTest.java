package com.sighware.mark.server.command;

import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.AddressUpdateEvent;
import com.sighware.mark.server.event.EntitlementCreateEvent;
import com.sighware.mark.server.event.RegistrationNumberEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.Seeder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RebuildCommandTest {

    static DynamoDBAdapter DB_ADAPTER = DynamoDBAdapter.getInstance();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void rebuild() throws InterruptedException, RegistrationNumberNotFoundException {

        RegistrationNumber regNum = Seeder.buildRegistrationNumber();

        RegistrationNumberEvent event = new EntitlementCreateEvent(regNum);
        Command ec = new EntitlementCreateCommand(event,
                DB_ADAPTER.getDynamoDBMapper());
        regNum = ec.persist();

        // Capture the mark and event time
        String mark = regNum.getMark();
        ZonedDateTime time1 = ZonedDateTime.parse(event.getCreateTime());
        String address1 = regNum.getEntitlement().getAddress().getAddLine1();

        Thread.sleep(25);

        // Capture change address
        String address2 = "5 Your Street";
        regNum.getEntitlement().getAddress().setAddLine1(address2);
        event = new AddressUpdateEvent(regNum);
        ec = new UpdateCommand(event,
                DB_ADAPTER.getDynamoDBMapper());
        ec.persist();

        ZonedDateTime time2 = ZonedDateTime.parse(event.getCreateTime());

        // Test for the first version of the RegistrationNumber
        RebuildCommand rebuild = new RebuildCommand(DB_ADAPTER.getDynamoDBMapper(), mark, time1);
        RegistrationNumber reg = rebuild.rebuild();
        assertEquals(address1, reg.getEntitlement().getAddress().getAddLine1());

        // Test for the last version of the RegistrationNumber
        rebuild = new RebuildCommand(DB_ADAPTER.getDynamoDBMapper(), mark);
        reg = rebuild.rebuild();
        assertEquals(address2, reg.getEntitlement().getAddress().getAddLine1());
    }

    @Test
    public void rebuildFail() {
        RebuildCommand rebuild = new RebuildCommand(DynamoDBAdapter.getInstance().getDynamoDBMapper(), "DOES NOT EXIST");
        try {
            rebuild.rebuild();
            Assertions.fail("Should be RegistrationNumberNotFoundException");
        } catch (RegistrationNumberNotFoundException e) {
        }
    }
}