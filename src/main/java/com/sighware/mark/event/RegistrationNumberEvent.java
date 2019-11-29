package com.sighware.mark.event;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.sighware.mark.model.RegistrationNumber;
import com.sighware.mark.model.RegistrationNumberConverter;
import com.sighware.mark.model.RegistrationNumberDocument;
import com.sighware.mark.model.RegistrationNumberTable;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@DynamoDBTable(tableName = "RegistrationNumberEvent")
public class RegistrationNumberEvent {
    private RegistrationNumberDocument registrationNumber;
    private String createTime;
    private String eventId;
    private String eventName;
    private String mark;

    public RegistrationNumberEvent(RegistrationNumber registrationNumber) {
        eventId = UUID.randomUUID().toString();
        createTime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        this.eventName = this.getClass().getSimpleName();
        this.mark = registrationNumber.getMark();
        this.registrationNumber = RegistrationNumberConverter.toDocument(registrationNumber);
        this.registrationNumber.setEventTime(getCreateTime());
    }

    @DynamoDBHashKey(attributeName = "mark")
    public String getMark() {
        return mark;
    }

    @DynamoDBRangeKey(attributeName = "createTime")
    public String getCreateTime() {
        return createTime;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventName() {
        return eventName;
    }

    @DynamoDBNamed("RegistrationNumber")
    public RegistrationNumberDocument getRegistrationNumber() {
        return registrationNumber;
    }

    @DynamoDBIgnore
    public RegistrationNumberTable getRegistrationNumberTable() {
        return RegistrationNumberConverter.toTable(registrationNumber);
    }
}
