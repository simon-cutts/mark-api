package com.sighware.mark.model;

/**
 * Converts between RegistrationNumberTable and RegistrationNumberDocument
 */
public class RegistrationNumberConverter {

    private RegistrationNumberConverter() {
    }

    public static com.sighware.mark.model.RegistrationNumberTable toTable(RegistrationNumber source) {
        com.sighware.mark.model.RegistrationNumberTable target = new com.sighware.mark.model.RegistrationNumberTable();
        convert(source, target);
        return target;
    }

    public static com.sighware.mark.model.RegistrationNumberDocument toDocument(RegistrationNumber source) {
        com.sighware.mark.model.RegistrationNumberDocument target = new com.sighware.mark.model.RegistrationNumberDocument();
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
