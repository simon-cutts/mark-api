package com.sighware.mark.server.query;

import com.sighware.mark.server.command.Command;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.command.UpdateCommand;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.AddressUpdateEvent;
import com.sighware.mark.server.event.EntitlementCreateEvent;
import com.sighware.mark.server.event.RegistrationNumberEvent;
import com.sighware.mark.server.event.RegistrationNumberEvents;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.Seeder;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventQueryTest {

    public static final DynamoDBAdapter DB_ADAPTER = DynamoDBAdapter.getInstance();

    @Test
    public void get() throws InterruptedException, RegistrationNumberNotFoundException {

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

        // Test just 1 event found for the first time
        EventQuery eq = new EventQuery(DB_ADAPTER.getDynamoDBMapper(), time1, mark);
        RegistrationNumberEvents list = eq.get();
        assertEquals(1, list.getEvents().size());
        assertEquals("EntitlementCreateEvent", list.getEvents().get(0).getEventName());

        // Test both events for latest time
        eq = new EventQuery(DB_ADAPTER.getDynamoDBMapper(), mark);
        list = eq.get();
        assertEquals(2, list.getEvents().size());
        assertEquals("EntitlementCreateEvent", list.getEvents().get(0).getEventName());
        assertEquals("AddressUpdateEvent", list.getEvents().get(1).getEventName());
    }

}