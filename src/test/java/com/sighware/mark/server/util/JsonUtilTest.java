package com.sighware.mark.server.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import com.sighware.mark.server.model.Status;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class JsonUtilTest {

    @Test
    void testJson() throws IOException {
        RegistrationNumber rg = Seeder.buildRegistrationNumber();
        String json = JsonUtil.toJsonPretty(rg);
        System.out.println(json);
        JsonUtil.toObject(json, RegistrationNumberDocument.class);

        ObjectMapper mapper = new ObjectMapper();

        Status s1 = mapper.readValue("\"MARK_AVAILABLE\"", Status.class);
        Status s2 = mapper.readValue("\"MARK_ASSIGNED\"", Status.class);
    }

}