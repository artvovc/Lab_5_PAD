package com.controllers;

import com.crud.CrudOperationMongoDB;
import com.model.Empl;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.util.JSONUtil;
import org.apache.log4j.Logger;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
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

            List<Empl> empls = new CrudOperationMongoDB().findOffsetLimit(values);

            String rns = JSONUtil.getJSONStringfromJAVAObject(empls);
            httpExchange.sendResponseHeaders(200, rns.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(rns.getBytes(), 0, rns.length());
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