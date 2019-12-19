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

    @DynamoDBHashKey(attributeName = "mark")
    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public Double getPrice() {
        return price;
    }

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

    public void setVersion(Long version) {
        this.version = version;
    }

    public Entitlement getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(Entitlement entitlement) {
        this.entitlement = entitlement;
    }
}
