package com.controllers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.Logger;

import java.io.IOException;

import static java.lang.String.format;

public class StatusHandler extends AbstractServerController implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(StatusHandler.class);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
        try{
            sendRNS(httpExchange, "OK", 200);
        }
        catch(Exception ex){
            ex.printStackTrace();
            try {
                sendRNS(httpExchange, "NOK", 500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
