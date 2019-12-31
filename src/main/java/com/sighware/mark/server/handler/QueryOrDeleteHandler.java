package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.Command;
import com.sighware.mark.server.command.DeleteCommand;
import com.sighware.mark.server.error.RegistrationNumberNotFoundException;
import com.sighware.mark.server.event.DeleteEvent;
import com.sighware.mark.server.query.RegistrationNumberQuery;
import com.sighware.mark.server.util.DynamoDBAdapter;
import com.sighware.mark.server.util.JsonUtil;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

public class QueryOrDeleteHandler extends Handler {

    public QueryOrDeleteHandler(DynamoDBAdapter adapter) {
        super(adapter);
    }

    @Override
    public AwsProxyResponse handle(AwsProxyRequest request) {
        // Get the mark from the path
        int s = request.getPath().lastIndexOf("/");
        int e = request.getPath().length();
        String mark = request.getPath().substring(s + 1, e);

        AwsProxyResponse response = new AwsProxyResponse();
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

        RegistrationNumberQuery query;

        try {

            if (request.getHttpMethod().equals(HttpMethod.GET)) {
                query = new RegistrationNumberQuery(mark, adapter.getDynamoDBMapper());

                response.setStatusCode(200);
                response.setBody(JsonUtil.toJson(query.get()));
                return response;

            } else if (request.getHttpMethod().equals(HttpMethod.DELETE)) {
                query = new RegistrationNumberQuery(mark, adapter.getDynamoDBMapper());

                // Create the command with the event
                Command command = new DeleteCommand(
                        new DeleteEvent(query.get()), adapter.getDynamoDBMapper());
                command.persist();
                response.setStatusCode(204);
                response.setBody("");
                return response;
            }

        } catch (RegistrationNumberNotFoundException ex) {
            response.setStatusCode(204);
            response.setBody("");
            log.info("Unable to find mark " + mark);
        }
        return new AwsProxyResponse(404);
    }
}