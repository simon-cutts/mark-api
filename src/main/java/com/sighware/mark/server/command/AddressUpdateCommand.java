package com.sighware.mark.server.command;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.sighware.mark.server.event.RegistrationNumberEvent;

/**
 * Entitlement create command to handle the persistence of both the Event and its payload.
 *
 * @author Simon Cutts
 */
public class AddressUpdateCommand extends Command {
    public AddressUpdateCommand(RegistrationNumberEvent event, DynamoDBMapper mapper) {
        super(event, mapper);
    }
}
