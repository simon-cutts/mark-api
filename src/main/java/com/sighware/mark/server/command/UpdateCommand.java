package com.sighware.mark.server.command;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.sighware.mark.server.event.RegistrationNumberEvent;

/**
 * General update command to handle the persistence of both the Event and its RegistrationNumber payload.
 *
 * @author Simon Cutts
 */
public class UpdateCommand extends Command {

    public UpdateCommand(RegistrationNumberEvent event, DynamoDBMapper mapper) {
        super(event, mapper);
    }
}
