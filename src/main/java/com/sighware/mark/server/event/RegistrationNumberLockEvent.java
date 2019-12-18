package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumber;

public class RegistrationNumberLockEvent extends RegistrationNumberEvent {

    public RegistrationNumberLockEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }
}
