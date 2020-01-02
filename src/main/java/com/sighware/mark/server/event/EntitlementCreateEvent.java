package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumber;

public class EntitlementCreateEvent extends RegistrationNumberEvent {

    public EntitlementCreateEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }

}
