package com.sighware.mark.model;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.DataStore;
import com.sighware.mark.data.DynamoDBAdapter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class EntitlementTest {

    private static final DynamoDBAdapter adapter = DynamoDBAdapter.getInstance(
            new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "localhost"));

    @BeforeAll
    static void setUp() {
        DataStore ds = new DataStore();
        ds.build();
    }

    @AfterAll
    static void tearDown() {
        DataStore ds = new DataStore();
//        ds.cleanUp();
    }

    @Test
    public void testEntitlement() throws Exception {

        RegistrationNumber m = buildMark();

        DynamoDBMapper mapper = adapter.getDynamoDBMapper();
        TransactionWriteRequest writeRequest = new TransactionWriteRequest();
        writeRequest.addPut(m);
        mapper.transactionWrite(writeRequest);
    }

    RegistrationNumber buildMark() {
        String time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

        RegistrationNumber m = new RegistrationNumber();
        m.setMark("AFC 7");
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

    public static void main(String[] args) {


        ObjectMapper objectMapper = new ObjectMapper();

        String time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);

        RegistrationNumber m = new RegistrationNumber();
        m.setMark("AFC 6");
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

        try {
            System.out.println(objectMapper.writeValueAsString(m));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }
}