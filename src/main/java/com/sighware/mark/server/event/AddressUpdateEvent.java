package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumber;

public class AddressUpdateEvent extends RegistrationNumberEvent {

    public AddressUpdateEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }
}
