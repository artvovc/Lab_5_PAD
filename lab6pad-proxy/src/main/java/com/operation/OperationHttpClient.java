package com.operation;

import com.model.Empl;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Random;

import static java.lang.String.format;

public class OperationHttpClient {

    private static int PORT = 9000;

    public OperationHttpClient() {
        boolean server_9000 = false;
        boolean server_9001 = false;
        PORT = 9000;
        String url = format("http://localhost:%d/status", PORT);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse closeableHttpResponse = client.execute(httpGet);
            server_9000 = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        PORT = 9001;
        url = format("http://localhost:%d/status", PORT);
        client = HttpClients.createDefault();
        httpGet = new HttpGet(url);
        try {
            CloseableHttpResponse closeableHttpResponse = client.execute(httpGet);
            server_9001 = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (server_9000 && server_9001)
            PORT = 9000 + new Random().nextInt(2);
        else if (server_9000)
            PORT = 9000;
        else if (server_9001)
            PORT = 9001;
        else PORT = 0;
    }

    public ReturnObject insertOne(Empl empl, String requestBody) throws Exception {
        if (PORT == 0) return new ReturnObject(500, "SERVER ERROR");
        String url = format("http://localhost:%d/employee/post", PORT);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        CloseableHttpResponse closeableHttpResponse = client.execute(httpPost);

        StringWriter writer = new StringWriter();
        IOUtils.copy(closeableHttpResponse.getEntity().getContent(), writer, "UTF-8");
        String response = writer.toString();

        client.close();

        int status = closeableHttpResponse.getStatusLine().getStatusCode();

        return new ReturnObject(status, response);
    }

    public ReturnObject findOneAndReplace(HttpExchange httpExchange, Empl empl, String requestBody) throws Exception {
        if (PORT == 0) return new ReturnObject(500, "SERVER ERROR");
        String url = format("http://localhost:%d/employee/put", PORT);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut httpPost = new HttpPut(url);
        StringEntity entity = new StringEntity(requestBody);
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("firstname", httpExchange.getRequestHeaders().get("firstname").get(0));
        CloseableHttpResponse closeableHttpResponse = client.execute(httpPost);

        StringWriter writer = new StringWriter();
        IOUtils.copy(closeableHttpResponse.getEntity().getContent(), writer, "UTF-8");
        String response = writer.toString();

        client.close();

        int status = closeableHttpResponse.getStatusLine().getStatusCode();

        return new ReturnObject(status, response);
    }

    public ReturnObject delete(String firstname) throws Exception {
        if (PORT == 0) return new ReturnObject(500, "SERVER ERROR");
        String url = format("http://localhost:%d/employee/delete", PORT);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPut httpPost = new HttpPut(url);
        httpPost.setHeader("firstname", firstname);
        CloseableHttpResponse closeableHttpResponse = client.execute(httpPost);

        StringWriter writer = new StringWriter();
        IOUtils.copy(closeableHttpResponse.getEntity().getContent(), writer, "UTF-8");
        String response = writer.toString();

        client.close();

        int status = closeableHttpResponse.getStatusLine().getStatusCode();

        return new ReturnObject(status, response);
    }

    public ReturnObject find() throws Exception {
        if (PORT == 0) return new ReturnObject(500, "SERVER ERROR");
        String url = format("http://localhost:%d/employee/get", PORT);
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

        return new ReturnObject(status, response);
    }

    public ReturnObject findOffsetLimit(Map<String, Integer> values, HttpExchange httpExchange) throws Exception {
        if (PORT == 0) return new ReturnObject(500, "SERVER ERROR");
        String url = format("http://localhost:%d/employee?%s", PORT, httpExchange.getRequestURI().getQuery());
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

        return new ReturnObject(status, response);
    }

}
