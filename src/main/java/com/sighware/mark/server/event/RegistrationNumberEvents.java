package com.sighware.mark.server.event;

import java.util.List;

public class RegistrationNumberEvents {
    private final List<RegistrationNumberEvent> events;

    public RegistrationNumberEvents(List<RegistrationNumberEvent> events) {
        this.events = events;
    }

    public List<RegistrationNumberEvent> getEvents() {
        return events;
    }
}
