package com.sighware.mark;

import com.sighware.mark.model.Entitlement;
import com.sighware.mark.model.RegistrationNumber;
import com.sighware.mark.model.RegistrationNumberInterface;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TestHelper {

    public static RegistrationNumberInterface buildRegistrationNumber() {
        String time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

        RegistrationNumberInterface m = new RegistrationNumber();
        m.setMark(randomString(20));
        m.setPrice(299.00);
        m.setEventTime(ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT));

        Entitlement ent = new Entitlement();
        ent.setCertificateNo("CERT2345");
        ent.setCertificateTime(time);
        ent.setAddLine1("2 My Street");
        ent.setAddLine2("Redwood");
        ent.setPostTown("Swansea");
        ent.setPostcode("SW1 4RT");
        ent.setNomineeName("Mr John Jones");
        ent.setPurchaserName("Felicity Jones");

        m.setEntitlement(ent);
        return m;
    }

    public static String randomString(int size) {
        final String alphabet = "0123456789ABCDEFGHJKLMPQRSTUVWXYZ";
        final int N = alphabet.length();

        Random r = new Random();
        String random = new String();
        for (int i = 0; i < size; i++) {
            random += alphabet.charAt(r.nextInt(N));
        }
        return random;
    }

    public static void main(String[] args) {
        TestHelper.randomString(20);
    }
}
