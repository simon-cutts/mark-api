package com.sighware.mark.server.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Allows a Address to be persisted as a json document in the RegistrationNumber table, wrapped
 * within a Entitlement within a RegistrationNumber
 *
 * @author Simon Cutts
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@DynamoDBDocument
public class Address {
    private String addLine1;
    private String addLine2;
    private String addLine3;
    private String addLine4;
    private String postTown;
    private String postcode;

    public String getAddLine1() {
        return addLine1;
    }

    public void setAddLine1(String addLine1) {
        this.addLine1 = addLine1;
    }

    public String getAddLine2() {
        return addLine2;
    }

    public void setAddLine2(String addLine2) {
        this.addLine2 = addLine2;
    }

    public String getAddLine3() {
        return addLine3;
    }

    public void setAddLine3(String addLine3) {
        this.addLine3 = addLine3;
    }

    public String getAddLine4() {
        return addLine4;
    }

    public void setAddLine4(String addLine4) {
        this.addLine4 = addLine4;
    }

    public String getPostTown() {
        return postTown;
    }

    public void setPostTown(String postTown) {
        this.postTown = postTown;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
