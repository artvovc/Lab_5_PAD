package com.controllers;

import com.Clients;
import com.operation.OperationHttpClient;
import com.operation.ReturnObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.lang.String.format;

public class GetProxyByOffsetAndLimitHandler extends AbstractProxyController implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(GetProxyByOffsetAndLimitHandler.class);

    private static ReturnObject rns = new ReturnObject(500, "SERVER ERROR");

    public void handle(HttpExchange httpExchange) {
        LOGGER.info(format("[PROXY] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
        while (Clients.getInstance().getHttpExchanges().size() == Clients.getInstance().getLIMIT()) {
            System.out.println("SOMEBODY WAIT");
        }
        Clients.getInstance().getHttpExchanges().add(httpExchange);
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

            if (!Objects.equals(returnObject.getRns(), rns.getRns())) {
                rns = new ReturnObject(returnObject.getStatus(), returnObject.getRns());
                h.add("FromProxy", "no");
                sendRNS(httpExchange, returnObject);
            } else {
                h.add("FromProxy", "yes");
                sendRNS(httpExchange, rns);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ex.printStackTrace();
                sendRNS(httpExchange, new ReturnObject(500, "SERVER ERROR"));
            } catch (Exception ex1) {
                ex.printStackTrace();
            }
        } finally {
            Clients.getInstance().getHttpExchanges().remove(httpExchange);
        }
    }
}

