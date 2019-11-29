package com.sighware.mark.server.model;

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

    private static void convert(RegistrationNumber source, RegistrationNumber target) {
        target.setMark(source.getMark());
        target.setEntitlement(source.getEntitlement());
        target.setPrice(source.getPrice());
        target.setStatus(source.getStatus());
        target.setVersion(source.getVersion());
        target.setEventTime(source.getEventTime());
    }
}
