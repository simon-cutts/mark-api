package com.sighware.mark.event;

import com.sighware.mark.model.RegistrationNumber;

public class EntitlementCreatedEvent extends RegistrationNumberEvent {

    public EntitlementCreatedEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }
}
