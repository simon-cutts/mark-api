package com.sighware.mark.server.command;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.RegistrationNumberEvent;
import com.sighware.mark.server.event.RegistrationNumberEvents;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberConverter;
import com.sighware.mark.server.model.RegistrationNumberDocument;
import com.sighware.mark.server.query.EventQuery;
import org.apache.log4j.Logger;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Rebuilds a RegistrationNumber by re-playing the event stream from a point in time. The persistence of the RegistrationNumber
 * in the database remains unaffected. The registration number is a snapshot view only from a point in time
 *
 * @author Simon Cutts
 */
public class RebuildCommand {

    private static final Logger LOG = Logger.getLogger(RebuildCommand.class);

    private static final String startTime = "1900-01-01T00:00:01.000Z";

    private final DynamoDBMapper mapper;
    private final String mark;
    private final String endTime;

    /**
     * Rebuild a registration number from before and up unto the timestamp
     *
     * @param mapper
     * @param mark
     * @param timestamp
     */
    public RebuildCommand(DynamoDBMapper mapper, String mark, ZonedDateTime timestamp) {
        this.mapper = mapper;
        this.mark = mark;
        this.endTime = timestamp.format(DateTimeFormatter.ISO_INSTANT);
    }

    /**
     * Rebuild a registration number from now
     *
     * @param mark
     * @param mapper
     */
    public RebuildCommand(DynamoDBMapper mapper, String mark) {
        this(mapper, mark, ZonedDateTime.now(ZoneOffset.UTC));
    }

    /**
     * Builds a view of registration number from a point in time
     */
    public RegistrationNumber rebuild() throws RegistrationNumberNotFoundException {

        LOG.info("Query registration number events with mark " + mark);
        EventQuery query = new EventQuery(mapper, ZonedDateTime.parse(endTime), mark);
        RegistrationNumberEvents events = query.get();

        // Create the target RegistrationNumber and collect the different versions of the registration number from the events
        RegistrationNumber regNum = null;
        List<RegistrationNumber> registrationNumbers = new ArrayList<>();
        for (RegistrationNumberEvent event : events.getEvents()) {

            // TODO: Is this needed?
            if (regNum == null) {
                if (event.getRegistrationNumber() != null) {
                    regNum = new RegistrationNumberDocument();
                }
            }

            registrationNumbers.add(event.getRegistrationNumber());
        }

        if (regNum == null) {
            throw new RegistrationNumberNotFoundException("Unable to find RegistrationNumber " + mark + " from " + endTime);
        }

        return RegistrationNumberConverter.snapshot(registrationNumbers);
    }
}
