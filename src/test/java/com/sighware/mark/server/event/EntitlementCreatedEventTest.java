package com.sighware.mark.server.event;

import com.sighware.mark.server.TestHelper;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.sighware.mark.server.util.JsonUtil.toJson;
import static com.sighware.mark.server.util.JsonUtil.toObject;

class EntitlementCreatedEventTest {

    @Test
    void testJson() {
        RegistrationNumber rn = TestHelper.buildRegistrationNumber();
        String json = toJson(new AddressUpdatedEvent(rn));
        toObject(json, RegistrationNumberEvent.class);
        try {
            toObject(json, RegistrationNumberDocument.class);
            Assertions.fail("Should be RuntimeException");
        } catch (RuntimeException e) {
        }
    }
}