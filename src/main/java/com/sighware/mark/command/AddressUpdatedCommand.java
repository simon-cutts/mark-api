package com.sighware.mark.command;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.sighware.mark.event.RegistrationNumberEvent;

/**
 * Entitlement create command to handle the persistence of both the Event and its payload.
 *
 * @author Simon Cutts
 */
public class AddressUpdatedCommand extends Command {
    public AddressUpdatedCommand(RegistrationNumberEvent event, DynamoDBMapper mapper) {
        super(event, mapper);
    }
}
