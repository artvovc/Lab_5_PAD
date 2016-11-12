package com.controllers;

import com.Clients;
import com.model.Empl;
import com.operation.OperationHttpClient;
import com.operation.ReturnObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.util.JSONUtil;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.StringWriter;

import static java.lang.String.format;

public class PostProxyHandler extends AbstractProxyController implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(PostProxyHandler.class);

    public void handle(HttpExchange httpExchange) {
        LOGGER.info(format("[PROXY] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
        while (Clients.getInstance().getHttpExchanges().size() == Clients.getInstance().getLIMIT()) {
            System.out.println("SOMEBODY WAIT");
        }
        Clients.getInstance().getHttpExchanges().add(httpExchange);
        try {
            InputStream is = httpExchange.getRequestBody();
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
            String requestBody = writer.toString();

            Empl empl = (Empl) JSONUtil.getJAVAObjectfromJSONString(requestBody, Empl.class);

            ReturnObject returnObject = new OperationHttpClient().insertOne(empl, requestBody);

            sendRNS(httpExchange, returnObject);
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