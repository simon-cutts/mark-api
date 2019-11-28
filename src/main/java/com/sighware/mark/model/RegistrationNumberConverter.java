package com.sighware.mark.model;

public class RegistrationNumberConverter {

    public static com.sighware.mark.model.RegistrationNumberTable toTable(RegistrationNumberInterface r) {
        com.sighware.mark.model.RegistrationNumberTable rne = new com.sighware.mark.model.RegistrationNumberTable();
        convert(r, rne);
        return rne;
    }

    public static com.sighware.mark.model.RegistrationNumberDocument toDocument(RegistrationNumberInterface r) {
        com.sighware.mark.model.RegistrationNumberDocument rne = new com.sighware.mark.model.RegistrationNumberDocument();
        convert(r, rne);
        return rne;
    }

    private static void convert(RegistrationNumberInterface r, RegistrationNumberInterface rne) {
        rne.setMark(r.getMark());
        rne.setEntitlement(r.getEntitlement());
        rne.setPrice(r.getPrice());
        rne.setStatus(r.getStatus());
        rne.setVersion(r.getVersion());
        rne.setEventTime(r.getEventTime());
    }
}
