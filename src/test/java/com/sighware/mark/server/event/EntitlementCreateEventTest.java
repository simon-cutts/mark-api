package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumberDocument;
import com.sighware.mark.server.util.Seeder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sighware.mark.server.util.JsonUtil.toJson;
import static com.sighware.mark.server.util.JsonUtil.toObject;

class EntitlementCreateEventTest {

    @Test
    void testJson() {
        String json = toJson(new AddressUpdateEvent(Seeder.buildRegistrationNumber()));
        toObject(json, RegistrationNumberEvent.class);
        try {
            toObject(json, RegistrationNumberDocument.class);
            Assertions.fail("Should be RuntimeException");
        } catch (RuntimeException e) {
        }
    }
}