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

import java.io.StringWriter;
import java.util.Map;
import java.util.Random;

import static java.lang.String.format;

public class OperationHttpClient {

    public ReturnObject insertOne(Empl empl, String requestBody) throws Exception {

        String url = format("http://localhost:%d/employee/post", (9000 + new Random().nextInt(2)));
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
        String url = format("http://localhost:%d/employee/put", (9000 + new Random().nextInt(2)));
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

        String url = format("http://localhost:%d/employee/delete", (9000 + new Random().nextInt(2)));
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

        return new ReturnObject(status, response);
    }

    public ReturnObject findOffsetLimit(Map<String, Integer> values, HttpExchange httpExchange) throws Exception {

        String url = format("http://localhost:%d/employee?%s", (9000 + new Random().nextInt(2)),
                httpExchange.getRequestURI().getQuery());
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
