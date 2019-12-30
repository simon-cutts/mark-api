package com.sighware.mark.server.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBVersionAttribute;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Allows a RegistrationNumber to be persisted directly in the RegistrationNumber table
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamoDBTable(tableName = "RegistrationNumber")
public class RegistrationNumberTable implements RegistrationNumber {
    private String mark;
    private String status;
    private String eventTime;
    private String lockTime;
    private Double price;
    private Long version;
    private Entitlement entitlement;

    @Override
    @DynamoDBHashKey(attributeName = "mark")
    public String getMark() {
        return mark;
    }

    @Override
    public void setMark(String mark) {
        this.mark = mark;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
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

    @DynamoDBVersionAttribute
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
