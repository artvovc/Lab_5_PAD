package com.controllers;

import com.operation.OperationHttpClient;
import com.operation.ReturnObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.apache.log4j.Logger;

import java.io.OutputStream;

import static java.lang.String.format;

public class DeleteProxyHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(PostProxyHandler.class);

    public void handle(HttpExchange httpExchange) {
        LOGGER.info(format("[PROXY] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
        try {
            ReturnObject returnObject = new OperationHttpClient().delete(httpExchange.getRequestHeaders().get("firstname").get(0));

            sendRNS(httpExchange,returnObject);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                ex.printStackTrace();
                sendRNS(httpExchange,new ReturnObject(500,"SERVER ERROR"));
            } catch (Exception ex1) {
                ex.printStackTrace();
            }
        }
    }

    private void sendRNS(HttpExchange httpExchange, ReturnObject returnObject) throws Exception {
        httpExchange.sendResponseHeaders(returnObject.getStatus(), returnObject.getRns().length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(returnObject.getRns().getBytes());
        os.close();
    }

}