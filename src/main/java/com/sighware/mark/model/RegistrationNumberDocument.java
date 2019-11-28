package com.sighware.mark.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamoDBDocument
public class RegistrationNumberDocument implements RegistrationNumberInterface {
    private String mark;
    private String status;
    private String eventTime;
    private Double price;
    private Long version;
    private Entitlement entitlement;

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

//    public static com.sighware.som.event.RegistrationNumber toTable(RegistrationNumber registrationNumber) {
//        com.sighware.som.event.RegistrationNumber rne = new com.sighware.som.event.RegistrationNumber();
//        rne.setMark(registrationNumber.getMark());
//        rne.setEntitlement(registrationNumber.getEntitlement());
//        rne.setPrice(registrationNumber.getPrice());
//        rne.setStatus(registrationNumber.getStatus());
//        rne.setVersion(registrationNumber.getVersion());
//        rne.setEventTime(registrationNumber.getEventTime());
//
//        return rne;
//    }
}
