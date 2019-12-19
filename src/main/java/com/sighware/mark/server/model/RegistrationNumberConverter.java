package com.sighware.mark.server.model;

import java.util.List;

/**
 * Converts between RegistrationNumberTable and RegistrationNumberDocument
 */
public class RegistrationNumberConverter {

    private RegistrationNumberConverter() {
    }

    public static RegistrationNumberTable toTable(RegistrationNumber source) {
        RegistrationNumberTable target = new RegistrationNumberTable();
        convert(source, target);
        return target;
    }

    public static RegistrationNumberDocument toDocument(RegistrationNumber source) {
        RegistrationNumberDocument target = new RegistrationNumberDocument();
        convert(source, target);
        return target;
    }

    public static RegistrationNumber snapshot(List<RegistrationNumber> registrationNumbers) {
        RegistrationNumber regnum = new RegistrationNumberDocument();

        for (RegistrationNumber registrationNumber : registrationNumbers) {
            convert(registrationNumber, regnum);
        }

        return regnum;
    }

    private static void convert(RegistrationNumber source, RegistrationNumber target) {
        target.setMark(source.getMark());
        target.setLockTime(source.getLockTime());
        target.setEntitlement(source.getEntitlement());
        target.setPrice(source.getPrice());
        target.setStatus(source.getStatus());
        target.setVersion(source.getVersion());
        target.setEventTime(source.getEventTime());
    }
}
