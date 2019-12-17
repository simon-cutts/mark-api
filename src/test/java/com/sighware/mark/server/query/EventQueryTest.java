package com.sighware.mark.server.query;

import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.command.AddressUpdateCommand;
import com.sighware.mark.server.command.Command;
import com.sighware.mark.server.command.EntitlementCreateCommand;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.AddressUpdatedEvent;
import com.sighware.mark.server.event.EntitlementCreatedEvent;
import com.sighware.mark.server.event.RegistrationNumberEvent;
import com.sighware.mark.server.event.RegistrationNumberEvents;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.util.DynamoDBAdapter;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventQueryTest {

    @Test
    public void get() throws InterruptedException, RegistrationNumberNotFoundException {

        RegistrationNumber regNum = TestHelper.buildRegistrationNumber();

        RegistrationNumberEvent event = new EntitlementCreatedEvent(regNum);
        Command ec = new EntitlementCreateCommand(event,
                DynamoDBAdapter.getInstance().getDynamoDBMapper());
        regNum = ec.persist();

        // Capture the mark and event time
        String mark = regNum.getMark();
        ZonedDateTime time1 = ZonedDateTime.parse(event.getCreateTime());
        String address1 = regNum.getEntitlement().getAddress().getAddLine1();

        Thread.sleep(25);

        // Capture change address
        String address2 = "5 Your Street";
        regNum.getEntitlement().getAddress().setAddLine1(address2);
        event = new AddressUpdatedEvent(regNum);
        ec = new AddressUpdateCommand(event,
                DynamoDBAdapter.getInstance().getDynamoDBMapper());
        regNum = ec.persist();

        ZonedDateTime time2 = ZonedDateTime.parse(event.getCreateTime());

        // Test just 1 event found for the first time
        EventQuery eq = new EventQuery(DynamoDBAdapter.getInstance().getDynamoDBMapper(), time1, mark);
        RegistrationNumberEvents list = eq.get();
        assertEquals(1, list.getEvents().size());
        assertEquals("EntitlementCreatedEvent", list.getEvents().get(0).getEventName());

        // Test both events for latest time
        eq = new EventQuery(DynamoDBAdapter.getInstance().getDynamoDBMapper(), mark);
        list = eq.get();
        assertEquals(2, list.getEvents().size());
        assertEquals("EntitlementCreatedEvent", list.getEvents().get(0).getEventName());
        assertEquals("AddressUpdatedEvent", list.getEvents().get(1).getEventName());

//        String json = JsonUtil.toJson(list);
//        System.out.println(json);
    }

}