package com.sighware.mark.server.command;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.sighware.mark.server.event.RegistrationNumberEvent;

/**
 * UnLock a mark
 *
 * @author Simon Cutts
 */
public class UnLockCommand extends Command {

    public UnLockCommand(RegistrationNumberEvent event, DynamoDBMapper mapper) {
        super(event, mapper);
    }

    public void process() {
        event.getRegistrationNumber().setLockTime(null);
    }
}
