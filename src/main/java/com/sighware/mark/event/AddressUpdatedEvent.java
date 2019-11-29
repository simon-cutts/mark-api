package com.sighware.mark.event;

import com.sighware.mark.model.RegistrationNumber;

public class AddressUpdatedEvent extends RegistrationNumberEvent {

    public AddressUpdatedEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }
}
