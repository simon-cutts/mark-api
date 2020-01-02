package com.sighware.mark.server.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Time {

    private Time() {
    }

    public static ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }

    public static ZonedDateTime toZonedDateTime(String time) {
        return ZonedDateTime.parse(time);
    }

    public static String getTimestamp() {
        return getZonedDateTime().format(DateTimeFormatter.ISO_INSTANT);
    }

    public static String getTimestamp(ZonedDateTime time) {
        return time.format(DateTimeFormatter.ISO_INSTANT);
    }

    public static String getTimestampPlus10Min() {
        return getZonedDateTime().plusMinutes(10).format(DateTimeFormatter.ISO_INSTANT);
    }
}
