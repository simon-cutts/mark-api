package com.sighware.mark.event;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.sighware.mark.model.RegistrationNumberConverter;
import com.sighware.mark.model.RegistrationNumberDocument;
import com.sighware.mark.model.RegistrationNumberInterface;
import com.sighware.mark.model.RegistrationNumberTable;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@DynamoDBTable(tableName = "RegistrationNumberEvent")
public class RegistrationNumberEvent {

    private RegistrationNumberInterface registrationNumber;
    private String createTime;
    private String eventId;
    private String eventName;
    private String mark;

    public RegistrationNumberEvent(RegistrationNumberInterface registrationNumber) {
        eventId = UUID.randomUUID().toString();
        createTime = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT);
        this.eventName = this.getClass().getSimpleName();
        this.mark = registrationNumber.getMark();
        this.registrationNumber = registrationNumber;
        this.registrationNumber.setEventTime(getCreateTime());
    }

    @DynamoDBHashKey(attributeName = "mark")
    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCreateTime() {
        return createTime;
    }

    @DynamoDBRangeKey(attributeName = "createTime")
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @DynamoDBNamed("RegistrationNumber")
    public RegistrationNumberDocument getRegistrationNumber() {
        return RegistrationNumberConverter.toDocument(registrationNumber);
    }

    @DynamoDBIgnore
    public RegistrationNumberTable getRegistrationNumberTable() {
        return RegistrationNumberConverter.toTable(registrationNumber);
    }
}
