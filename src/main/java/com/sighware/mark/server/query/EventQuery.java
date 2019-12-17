package com.sighware.mark.server.query;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.RegistrationNumberEvent;
import com.sighware.mark.server.event.RegistrationNumberEvents;
import org.apache.log4j.Logger;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Event query to retrieve all the events of a registration number
 *
 * @author Simon Cutts
 */
public class EventQuery {

    private static final Logger LOG = Logger.getLogger(EventQuery.class);

    private final ZonedDateTime startTime = ZonedDateTime.parse("1900-01-01T00:00:01.000Z");
    private final DynamoDBMapper mapper;
    private final String mark;
    private final ZonedDateTime endTime;

    /**
     * Create an instance of EventQuery with an endTime of now
     *
     * @param mapper
     * @param mark
     */
    public EventQuery(DynamoDBMapper mapper, String mark) {
        this(mapper, ZonedDateTime.now(ZoneOffset.UTC), mark);
    }

    /**
     * Create an instance of EventQuery for a specified endTime
     *
     * @param mapper
     * @param endTime
     * @param mark
     */
    public EventQuery(DynamoDBMapper mapper, ZonedDateTime endTime, String mark) {
        this.mapper = mapper;
        this.endTime = endTime;
        this.mark = mark;
    }

    /**
     * Get the registration number events
     */
    public RegistrationNumberEvents get() throws RegistrationNumberNotFoundException {

        // Retrieve the RegistrationNumberEvents
        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":mark", new AttributeValue().withS(mark));
        eav.put(":startTime", new AttributeValue().withS(startTime.format(DateTimeFormatter.ISO_INSTANT)));
        eav.put(":endTime", new AttributeValue().withS(endTime.format(DateTimeFormatter.ISO_INSTANT)));

        DynamoDBQueryExpression<RegistrationNumberEvent> queryExpression = new DynamoDBQueryExpression<RegistrationNumberEvent>()
                .withKeyConditionExpression("mark = :mark and createTime BETWEEN :startTime AND :endTime")
                .withExpressionAttributeValues(eav);

        LOG.info("Query registration number events with mark " + mark);

        try {
            List<RegistrationNumberEvent> events = mapper.query(RegistrationNumberEvent.class, queryExpression);
            return new RegistrationNumberEvents(events);
        } catch (Exception e) {
            throw new RegistrationNumberNotFoundException("Unable to find registration number " + mark, e);
        }
    }
}
