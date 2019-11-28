package com.sighware.mark.command;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.sighware.mark.event.RegistrationNumberEvent;
import com.sighware.mark.model.RegistrationNumberInterface;
import org.apache.log4j.Logger;


/**
 * Customer create command to handle the persistence of both the Event and its payload.
 *
 * @author Simon Cutts
 */
public class EntitlementCreateCommand {

    private static final Logger log = Logger.getLogger(EntitlementCreateCommand.class);

    private final DynamoDBMapper mapper;

    private final RegistrationNumberEvent event;

    public EntitlementCreateCommand(RegistrationNumberEvent event, DynamoDBMapper mapper) {
        this.event = event;
        this.mapper = mapper;
    }

    /**
     * Persist the event and customer data. Uses AWS Transaction support for DynamoDBMapper, so both
     * tables are saved as one transaction
     */
    public RegistrationNumberInterface persist() {
        log.info("persist");

        TransactionWriteRequest writeRequest = new TransactionWriteRequest();
        writeRequest.addPut(event.getRegistrationNumberTable());
        writeRequest.addPut(event);
        mapper.transactionWrite(writeRequest);

        return event.getRegistrationNumber();
    }
}
