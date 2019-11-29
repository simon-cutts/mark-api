package com.sighware.mark.command;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.sighware.mark.event.RegistrationNumberEvent;
import com.sighware.mark.model.RegistrationNumber;
import org.apache.log4j.Logger;

/**
 * Entitlement create command to handle the persistence of both the Event and its payload.
 *
 * @author Simon Cutts
 */
public class Command {
    static final Logger log = Logger.getLogger(Command.class);

    private final DynamoDBMapper mapper;
    private final RegistrationNumberEvent event;

    public Command(RegistrationNumberEvent event, DynamoDBMapper mapper) {
        this.event = event;
        this.mapper = mapper;
    }

    /**
     * Persist into RegistrationNumberEvent and RegistrationNumber tables in one transaction
     */
    public RegistrationNumber persist() {
        log.info("persist");

        TransactionWriteRequest writeRequest = new TransactionWriteRequest();

        writeRequest.addPut(event.getRegistrationNumberTable());

        // Ensure the version number matches across the RegistrationNumber in both
        // tables
        if (event.getRegistrationNumber().getVersion() == null) {
            event.getRegistrationNumber().setVersion(new Long(1));
        } else {
            Long version = (event.getRegistrationNumber().getVersion() + 1);
            event.getRegistrationNumber().setVersion(version);
        }
        writeRequest.addPut(event);
        mapper.transactionWrite(writeRequest);

        return event.getRegistrationNumber();
    }
}
