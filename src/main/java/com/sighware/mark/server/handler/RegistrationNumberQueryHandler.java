package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.query.RegistrationNumberQuery;
import com.sighware.mark.server.util.JsonUtil;
import org.apache.log4j.Logger;

import javax.ws.rs.HttpMethod;

public class RegistrationNumberQueryHandler {
    protected static final Logger log = Logger.getLogger(RegistrationNumberQueryHandler.class);

    protected DynamoDBAdapter adapter;

    public RegistrationNumberQueryHandler(DynamoDBAdapter adapter) {
        this.adapter = adapter;
    }

    public AwsProxyResponse handle(AwsProxyRequest request) {

        if (request.getHttpMethod().equals(HttpMethod.GET)) {

            // Get the mark from the path
            int s = request.getPath().lastIndexOf("/");
            int e = request.getPath().length();
            String mark = request.getPath().substring(s + 1, e);

            AwsProxyResponse response = new AwsProxyResponse();
            response.addHeader("Content-Type", "application/toJson");

            try {
                RegistrationNumberQuery query = new RegistrationNumberQuery(mark, adapter.getDynamoDBMapper());
                response.setStatusCode(200);
                response.setBody(JsonUtil.toJson(query.get()));

            } catch (RegistrationNumberNotFoundException ex) {
                response.setStatusCode(204);
                log.info("Unable to find mark " + mark);
            }

            return response;
        }
        throw new RuntimeException("Not implemented");
    }
}