package com.sighware.mark.event;

import com.sighware.mark.model.RegistrationNumberInterface;

public class EntitlementCreatedEvent extends RegistrationNumberEvent {

    public EntitlementCreatedEvent(RegistrationNumberInterface registrationNumber) {
        super(registrationNumber);
    }
}
