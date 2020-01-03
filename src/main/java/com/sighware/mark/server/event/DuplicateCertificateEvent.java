package com.sighware.mark.server.event;

import com.sighware.mark.server.model.RegistrationNumber;

public class DuplicateCertificateEvent extends RegistrationNumberEvent {

    public DuplicateCertificateEvent(RegistrationNumber registrationNumber) {
        super(registrationNumber);
    }
}
