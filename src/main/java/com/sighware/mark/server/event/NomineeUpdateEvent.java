package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumber;

public class NomineeUpdateEvent extends RegistrationNumberEvent {

    public NomineeUpdateEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }
}
