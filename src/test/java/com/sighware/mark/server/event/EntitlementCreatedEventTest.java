package com.sighware.mark.server.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.command.AddressUpdatedCommand;
import com.sighware.mark.server.model.RegistrationNumber;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class EntitlementCreatedEventTest {

    @Test
    void testJson() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        RegistrationNumber rn = TestHelper.buildRegistrationNumber();
        AddressUpdatedEvent event = new AddressUpdatedEvent(rn);
        String json = objectMapper.writeValueAsString(event);
        System.out.println(json);
        RegistrationNumberEvent rv = objectMapper.readValue(json, RegistrationNumberEvent.class);
    }
}