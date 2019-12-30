package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumber;

public class UnLockEvent extends RegistrationNumberEvent {

    public UnLockEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }
}
