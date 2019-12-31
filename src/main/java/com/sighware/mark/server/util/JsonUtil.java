package com.sighware.mark.server.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object object) {
        if (object == null) return "";
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static String toJsonPretty(Object object) {
        if (object == null) return "";
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static <T> T toObject(String json, Class<T> target) {
        try {
            return mapper.readValue(json, target);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
