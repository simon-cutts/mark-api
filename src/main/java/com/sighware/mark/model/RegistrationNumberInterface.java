package com.sighware.mark.model;

public interface RegistrationNumberInterface {

    public String getMark();

    public void setMark(String mark);

    public String getStatus();

    public void setStatus(String status);

    public String getEventTime();

    public void setEventTime(String eventTime);

    public Double getPrice();

    public void setPrice(Double price);

    public Long getVersion();

    public void setVersion(Long version);

    public Entitlement getEntitlement();

    public void setEntitlement(Entitlement entitlement);

}
