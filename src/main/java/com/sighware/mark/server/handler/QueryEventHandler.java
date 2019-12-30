package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.query.EventQuery;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;
import org.apache.log4j.Logger;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class QueryEventHandler {
    protected static final Logger log = Logger.getLogger(QueryEventHandler.class);

    protected DynamoDBAdapter adapter;

    public QueryEventHandler(DynamoDBAdapter adapter) {
        this.adapter = adapter;
    }

    public AwsProxyResponse handle(AwsProxyRequest request) {

        if (request.getHttpMethod().equals(HttpMethod.GET)) {

            // Get the mark from the path
            int s = request.getPath().lastIndexOf("/");
            int e = request.getPath().length();
            String mark = request.getPath().substring(s + 1, e);

            AwsProxyResponse response = new AwsProxyResponse();
            response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

            try {
                EventQuery query = new EventQuery(adapter.getDynamoDBMapper(), mark);
                response.setStatusCode(200);
                response.setBody(JsonUtil.toJson(query.get()));

            } catch (RegistrationNumberNotFoundException ex) {
                response.setStatusCode(204);
                response.setBody("");
                log.info("Unable to find mark " + mark);
            }

            return response;
        }
        throw new RuntimeException("Not implemented");
    }
}