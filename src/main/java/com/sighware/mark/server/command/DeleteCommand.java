package com.sighware.mark.server.command;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.TransactionWriteRequest;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.RegistrationNumberEvent;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.query.EventQuery;
import com.sighware.mark.server.query.RegistrationNumberQuery;

import java.util.List;

/**
 * Delete command to remove both RegistrationNumber and all its RegistrationNumberEvents in a single
 * atomic transaction
 *
 * @author Simon Cutts
 */
public class DeleteCommand extends Command {
    public DeleteCommand(RegistrationNumberEvent event, DynamoDBMapper mapper) {
        super(event, mapper);
    }

    @Override
    public RegistrationNumber persist() {
        log.info("Delete mark: " + event.getRegistrationNumber().getMark());

        try {
            RegistrationNumberQuery cc = new RegistrationNumberQuery(event.getMark(), mapper);
            RegistrationNumber regnum = cc.get();

            TransactionWriteRequest writeRequest = new TransactionWriteRequest();
            writeRequest.addDelete(regnum);

            // Now get the events to delete
            EventQuery query = new EventQuery(mapper, event.getMark());
            List<RegistrationNumberEvent> events = query.get().getEvents();
            for (RegistrationNumberEvent evt : events) {
                writeRequest.addDelete(evt);
            }

            mapper.transactionWrite(writeRequest);
        } catch (RegistrationNumberNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return null;
    }
}
