package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumber;

public class EntitlementExtendEvent extends RegistrationNumberEvent {

    public EntitlementExtendEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }
}
