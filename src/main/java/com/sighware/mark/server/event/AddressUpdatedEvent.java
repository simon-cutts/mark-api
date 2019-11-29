package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumber;

public class AddressUpdatedEvent extends RegistrationNumberEvent {

    public AddressUpdatedEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }
}
