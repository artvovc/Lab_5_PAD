package com.controllers;

import com.operation.OperationHttpClient;
import com.operation.ReturnObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class GetProxyByOffsetAndLimitHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(GetProxyByOffsetAndLimitHandler.class);

    public void handle(HttpExchange httpExchange) {
        LOGGER.info(format("[PROXY] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
        try {
            Headers h = httpExchange.getResponseHeaders();
            h.add("Content-Type", "application/json");

            String query = httpExchange.getRequestURI().getQuery();

            String[] variables = query.split("&");

            Map<String, Integer> values = new HashMap<>();

            for (String variable : variables) {
                String[] varAndValue = variable.split("=");
                values.put(varAndValue[0], Integer.parseInt(varAndValue[1]));
            }

            ReturnObject returnObject = new OperationHttpClient().findOffsetLimit(values, httpExchange);

            httpExchange.sendResponseHeaders(returnObject.getStatus(), returnObject.getRns().length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(returnObject.getRns().getBytes());
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ex.printStackTrace();
                String msg = "SERVER ERROR";
                httpExchange.sendResponseHeaders(500, msg.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(msg.getBytes(), 0, msg.length());
                os.close();
            } catch (Exception ex1) {
                ex.printStackTrace();
            }
        }

    }
}