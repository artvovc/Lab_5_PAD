package com.controllers;

import com.crud.CrudOperationMongoDB;
import com.model.Empl;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.util.JSONUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Random;

import static java.lang.String.format;

public class GetProxyHandler implements HttpHandler {
    private static final Logger LOGGER = Logger.getLogger(GetProxyHandler.class);

    public void handle(HttpExchange httpExchange) {
        LOGGER.info(format("[PROXY] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
        try {
            Headers h = httpExchange.getResponseHeaders();
            h.add("Content-Type", "application/json");

            String url = format("http://localhost:%d/employee/get", (9000 + new Random().nextInt(2)));
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            CloseableHttpResponse closeableHttpResponse = client.execute(httpGet);

            StringWriter writer = new StringWriter();
            IOUtils.copy(closeableHttpResponse.getEntity().getContent(), writer, "UTF-8");
            String response = writer.toString();

            client.close();

            int status = closeableHttpResponse.getStatusLine().getStatusCode();
            httpExchange.sendResponseHeaders(status, response.length());
            OutputStream os = httpExchange.getResponseBody();
            os.write(response.getBytes());
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