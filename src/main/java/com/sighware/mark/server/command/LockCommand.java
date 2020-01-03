package com.sighware.mark.server.command;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.sighware.mark.server.error.LockFailedException;
import com.sighware.mark.server.event.RegistrationNumberEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.Status;
import com.sighware.mark.server.util.Time;

import java.time.ZonedDateTime;

/**
 * Lock an available mark, with an expiration time of 10 minutes plus from now
 *
 * @author Simon Cutts
 */
public class LockCommand extends Command {

    public LockCommand(RegistrationNumberEvent event, DynamoDBMapper mapper) {
        super(event, mapper);
    }

    public void process() throws LockFailedException {

        RegistrationNumber target = event.getRegistrationNumber();

        if (!target.getStatus().equals(Status.MARK_AVAILABLE)) {
            throw new LockFailedException("RegistrationNumber " + target.getMark()
                    + " is not available for sale");
        }

        if (target.getLockTime() != null) {
            ZonedDateTime expired = Time.toZonedDateTime(target.getLockTime());
            if (expired.isAfter(Time.getZonedDateTime())) {
                throw new LockFailedException("RegistrationNumber " + target.getMark()
                        + " cannot be set, has an existing lock time of " + target.getLockTime());
            }
        }

        event.getRegistrationNumber().setLockTime(Time.getTimestampPlus10Min());
    }
}
