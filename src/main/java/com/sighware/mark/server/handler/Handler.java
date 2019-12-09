package com.sighware.mark.server.handler;

import com.amazonaws.serverless.proxy.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.model.AwsProxyResponse;
import com.sighware.mark.server.command.Command;
import com.sighware.mark.server.model.Error;
import com.sighware.mark.server.util.DynamoDBAdapter;
import org.apache.log4j.Logger;
import static com.sighware.mark.server.util.JsonUtil.toJson;

import java.io.IOException;

/**
 * Parent Handler for managing interaction between HTTP and business logic
 */
public abstract class Handler {
    protected static final Logger log = Logger.getLogger(Handler.class);

    protected DynamoDBAdapter adapter;

    public Handler(DynamoDBAdapter adapter) {
        this.adapter = adapter;
    }

    protected AwsProxyResponse getAwsProxyResponse(Command command, int statusCode) {
        String json;
        AwsProxyResponse response = new AwsProxyResponse();
        response.addHeader("Content-Type", "application/toJson");
        try {
            json = toJson(command.persist());
            response.setStatusCode(statusCode);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setStatusCode(500);
            json = toJson(new Error("500", "Internal Server Error", "Internal Server Error"));
        }
        response.setBody(json);
        return response;
    }

    public abstract AwsProxyResponse handle(AwsProxyRequest request) throws IOException;
}