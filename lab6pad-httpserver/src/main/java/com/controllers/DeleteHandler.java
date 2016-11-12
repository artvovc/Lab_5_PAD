package com.controllers;

import com.crud.CrudOperationMongoDB;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.Logger;

import static java.lang.String.format;

public class DeleteHandler extends AbstractServerController implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(PostHandler.class);

    public void handle(HttpExchange httpExchange) {
        LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
        try {
            String response = "was deleted tuple";
            int status = 200;
            new CrudOperationMongoDB().delete(httpExchange.getRequestHeaders().get("firstname").get(0));
            sendRNS(httpExchange, response, status);
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