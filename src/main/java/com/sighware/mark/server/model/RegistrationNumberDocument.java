package com.sighware.mark.server.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Allows a RegistrationNumber to be persisted as a json document in the RegistrationNumberEvent table, wrapped
 * within an event
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamoDBDocument
public class RegistrationNumberDocument implements RegistrationNumber {
    private String mark;
    private Status status;
    private String eventTime;
    private String lockTime;
    private Double price;
    private Long version;
    private Entitlement entitlement;

    @Override
    public String getMark() {
        return mark;
    }

    @Override
    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName="Status")
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String getEventTime() {
        return eventTime;
    }

    @Override
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String getLockTime() {
        return lockTime;
    }

    @Override
    public void setLockTime(String lockTime) {
        this.lockTime = lockTime;
    }

    @Override
    public Long getVersion() {
        return version;
    }

    @Override
    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public Entitlement getEntitlement() {
        return entitlement;
    }

    @Override
    public void setEntitlement(Entitlement entitlement) {
        this.entitlement = entitlement;
    }
}
