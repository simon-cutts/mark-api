package com.sighware.mark.server.model;

public interface RegistrationNumber {

    String getMark();

    void setMark(String mark);

    Status getStatus();

    void setStatus(Status status);

    String getEventTime();

    void setEventTime(String eventTime);

    Double getPrice();

    void setPrice(Double price);

    String getLockTime();

    void setLockTime(String lockTime);

    Long getVersion();

    void setVersion(Long version);

    Entitlement getEntitlement();

    void setEntitlement(Entitlement entitlement);

}
