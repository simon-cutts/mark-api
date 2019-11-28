package com.sighware.mark;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import com.sighware.mark.data.DynamoDBAdapter;

import java.util.Arrays;

public class DataStore {

    private static DynamoDBAdapter db = DynamoDBAdapter.getInstance(
            new AwsClientBuilder.EndpointConfiguration("http://localhost:8000", "localhost"));

    public void build() {

        Table table = db.getDynamoDB().createTable(DynamoDBAdapter.REGISTRATION_NUMBER_TABLE,
                Arrays.asList(new KeySchemaElement("mark", KeyType.HASH),
                        new KeySchemaElement("eventTime", KeyType.RANGE)),
                Arrays.asList(new AttributeDefinition("mark", ScalarAttributeType.S),
                        new AttributeDefinition("eventTime", ScalarAttributeType.S)),
                new ProvisionedThroughput(2L, 12L));
        try {
            System.out.println("Attempting to create table " + DynamoDBAdapter.REGISTRATION_NUMBER_TABLE);
            table.waitForActive();
            System.out.println("Success.  Table status: " + table.getDescription().getTableStatus());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUp() {
        Table table = db.getDynamoDB().getTable(DynamoDBAdapter.REGISTRATION_NUMBER_TABLE);
        try {
            System.out.println("Attempting to delete table " + DynamoDBAdapter.REGISTRATION_NUMBER_TABLE);
            table.delete();
            table.waitForDelete();
            System.out.println("Success.");

        } catch (Exception e) {
            System.err.println("Unable to delete table: ");
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        DataStore ds = new DataStore();
        ds.cleanUp();
        ds.build();
    }
}
