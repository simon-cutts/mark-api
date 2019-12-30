package com.sighware.mark.server.event;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberConverter;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import com.sighware.mark.server.model.RegistrationNumberTable;

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

    public RegistrationNumberEvent() {
    }

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

    public void setMark(String mark) {
        this.mark = mark;
    }

    @DynamoDBRangeKey(attributeName = "createTime")
    public String getCreateTime() {
        return createTime;
    }

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
    @JsonProperty("RegistrationNumber")
    public RegistrationNumberDocument getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(RegistrationNumberDocument registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @DynamoDBIgnore
    @JsonIgnore
    public RegistrationNumberTable getRegistrationNumberTable() {
        return RegistrationNumberConverter.toTable(registrationNumber);
    }
}
