package com.sighware.mark.server.command;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.sighware.mark.server.error.LockFailedException;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.RegistrationNumberEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.query.RegistrationNumberQuery;
import com.sighware.mark.server.util.Time;

/**
 * Update command to handle the persistence of both the Event and its payload.
 *
 * @author Simon Cutts
 */
public class RegistrationNumberLockCommand extends Command {
    public RegistrationNumberLockCommand(RegistrationNumberEvent event, DynamoDBMapper mapper) {
        super(event, mapper);
    }

    public void process() throws RegistrationNumberNotFoundException, LockFailedException {

        RegistrationNumberQuery query = new RegistrationNumberQuery(event.getMark(), mapper);
        RegistrationNumber target = query.get();

        if (target.getLockTime() != null) {
            throw new LockFailedException("RegistrationNumber " + target.getMark()
                    + " cannot be set, has an existing lock time of " + target.getLockTime());
        }

        event.getRegistrationNumber().setLockTime(Time.getTimestampNow());
    }
}
