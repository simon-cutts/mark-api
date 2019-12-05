package com.sighware.mark.server.query;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.model.RegistrationNumber;
import com.sighware.mark.server.model.RegistrationNumberTable;
import org.apache.log4j.Logger;

/**
 * Customer query to retrieve a RegistrationNumber
 *
 * @author Simon Cutts
 */
public class RegistrationNumberQuery {
    protected static final Logger log = Logger.getLogger(RegistrationNumberQuery.class);

    private final DynamoDBMapper mapper;
    private final String mark;

    public RegistrationNumberQuery(String mark, DynamoDBMapper mapper) {
        this.mark = mark;
        this.mapper = mapper;
    }

    /**
     * Get the RegistrationNumber
     */
    public RegistrationNumber get() throws RegistrationNumberNotFoundException {
        try {
            return mapper.load(RegistrationNumberTable.class, mark);
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RegistrationNumberNotFoundException("Unable to find registration number " + mark, e);
        }
    }
}
