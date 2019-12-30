package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumber;

public class LockEvent extends RegistrationNumberEvent {

    public LockEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }
}
