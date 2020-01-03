package com.sighware.mark.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.server.model.*;

import java.util.Random;

/**
 * Utility Seeder class to create registration numbers. Only to be used for test scenarios
 */
public class Seeder {

    private Seeder() {
    }

    public static RegistrationNumber buildRegistrationNumber() {

        RegistrationNumber m = buildRegistrationNumberSimple();

        m.setStatus(Status.MARK_ASSIGNED);
        Entitlement ent = new Entitlement();
        ent.setNomineeName("Mr John Jones");
        ent.setPurchaserName("Felicity Jones");
        ent.setCertificateNo(randomString(15));
        ent.setCertificateTime(Time.getTimestamp(Time.getZonedDateTime().plusYears(10)));
        ent.setAgreementTime(Time.getTimestamp());

        Address add = new Address();
        add.setAddLine1("2 My Street");
        add.setAddLine2("Redwood");
        add.setPostTown("Swansea");
        add.setPostcode("SW1 4RT");
        ent.setAddress(add);

        m.setEntitlement(ent);
        return m;
    }

    public static RegistrationNumber buildRegistrationNumberSimple() {

        RegistrationNumber m = new RegistrationNumberDocument();
        m.setMark(randomString(10));
        m.setPrice(299.00);
        m.setEventTime(Time.getTimestamp());
        m.setStatus(Status.MARK_AVAILABLE);

        return m;
    }

    public static String randomString(int size) {
        final String alphabet = "12345789ABCEFGHJLMPQR" +
                "STUVWYZ";
        final int N = alphabet.length();

        Random r = new Random();
        String random = "";
        for (int i = 0; i < size; i++) {
            random += alphabet.charAt(r.nextInt(N));
        }
        return random;
    }

    public static void main(String[] args) throws JsonProcessingException {
        System.out.println(Seeder.randomString(20));
        System.out.println(new ObjectMapper().writeValueAsString(Seeder.buildRegistrationNumber()));
    }
}
