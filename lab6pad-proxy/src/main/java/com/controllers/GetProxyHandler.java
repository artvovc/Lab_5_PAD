package com.controllers;

import com.Clients;
import com.operation.OperationHttpClient;
import com.operation.ReturnObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;

import java.util.Objects;

import static java.lang.String.format;

public class GetProxyHandler extends AbstractProxyController implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(GetProxyHandler.class);

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

            ReturnObject returnObject = new OperationHttpClient().find();

            Jedis jedis = new Jedis("localhost");

            if (!Objects.equals(jedis.get("data"), returnObject.getRns())) {
                jedis.setex("data", 10, returnObject.getRns());
                h.add("FromProxy", "no");
                sendRNS(httpExchange, returnObject);
            } else {
                h.add("FromProxy", "yes");
                if (jedis.exists("data"))
                    sendRNS(httpExchange, new ReturnObject(200, jedis.get("data")));
                else {
                    h.add("FromProxy", "no");
                    sendRNS(httpExchange, returnObject);
                }
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