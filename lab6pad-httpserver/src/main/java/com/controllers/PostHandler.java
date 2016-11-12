package com.controllers;

import com.crud.CrudOperationMongoDB;
import com.model.Empl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.util.JSONUtil;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.StringWriter;

import static java.lang.String.format;

public class PostHandler extends AbstractServerController implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(PostHandler.class);

    public void handle(HttpExchange httpExchange) {
        LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
        try {
            InputStream is = httpExchange.getRequestBody();
            StringWriter writer = new StringWriter();
            IOUtils.copy(is, writer, "UTF-8");
            String requestBody = writer.toString();
            Empl empl = (Empl) JSONUtil.getJAVAObjectfromJSONString(requestBody, Empl.class);
            String response = "tuple was inserted";
            int status = 200;
            new CrudOperationMongoDB().insertOne(empl);
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