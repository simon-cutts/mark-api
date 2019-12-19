package com.sighware.mark.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.server.model.Address;
import com.sighware.mark.server.model.Entitlement;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import com.sighware.mark.server.util.Time;

import java.util.Random;

public class TestHelper {

    private TestHelper() {
    }

    public static RegistrationNumber buildRegistrationNumber() {

        RegistrationNumber m = buildRegistrationNumberSimple();

        Entitlement ent = new Entitlement();
        ent.setNomineeName("Mr John Jones");
        ent.setPurchaserName("Felicity Jones");
        ent.setCertificateNo(randomString(15));
        ent.setCertificateTime(m.getEventTime());

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
        m.setEventTime(Time.getTimestampNow());

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
        System.out.println(TestHelper.randomString(20));
        System.out.println(new ObjectMapper().writeValueAsString(TestHelper.buildRegistrationNumber()));
    }
}
