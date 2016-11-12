package com.controllers;

import com.crud.CrudOperationMongoDB;
import com.model.Empl;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.util.JSONUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class GetByOffsetAndLimitHandler extends AbstractServerController implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(GetByOffsetAndLimitHandler.class);

    public void handle(HttpExchange httpExchange) {
        LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
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

            List<Empl> empls = new CrudOperationMongoDB().findOffsetLimit(values);

            String response = JSONUtil.getJSONStringfromJAVAObject(empls);
            sendRNS(httpExchange, response, 200);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ex.printStackTrace();
                sendRNS(httpExchange, "SERVER ERROR", 500);
            } catch (Exception ex1) {
                ex.printStackTrace();
            }
        }
    }
}