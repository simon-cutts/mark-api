package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumber;

public class EntitlementCreatedEvent extends RegistrationNumberEvent {

    public EntitlementCreatedEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }

}
