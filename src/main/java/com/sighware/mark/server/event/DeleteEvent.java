package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumber;

public class DeleteEvent extends RegistrationNumberEvent {

    public DeleteEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }
}
