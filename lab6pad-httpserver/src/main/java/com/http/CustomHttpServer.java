package com.http;

import com.model.Empl;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.util.JSONUtil;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;

public class CustomHttpServer {


    private static final Logger LOGGER = Logger.getLogger(CustomHttpServer.class);

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(9000), 0);
        server.createContext("/employee/post", new PostHandler());
        server.createContext("/employee/get", new GetHandler());
        server.createContext("/employee/put", new PutHandler());
        server.createContext("/employee/delete", new DeleteHandler());
        server.createContext("/employee", new GetByOffsetAndLimitHandler());
        server.setExecutor(null);
        server.start();
        LOGGER.info("[SERVER] --> WAS STARTED SERVER");
    }

    private static class PostHandler implements HttpHandler {
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

                MongoClient mongoClient = new MongoClient();
                MongoDatabase mongoDatabase = mongoClient.getDatabase("lab6pad");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("pad");

                mongoCollection.insertOne(empl.getDocument());

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

    private static class PutHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                    httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
            try {
                InputStream is = httpExchange.getRequestBody();
                StringWriter writer = new StringWriter();
                IOUtils.copy(is, writer, "UTF-8");
                String requestBody = writer.toString();

                Empl empl = (Empl) JSONUtil.getJAVAObjectfromJSONString(requestBody, Empl.class);
                String response = "was updated tuple";
                int status = 200;

                MongoClient mongoClient = new MongoClient();
                MongoDatabase mongoDatabase = mongoClient.getDatabase("lab6pad");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("pad");

                mongoCollection.findOneAndReplace(
                        new BsonDocument("firstname", new BsonString(httpExchange.getRequestHeaders().get("firstname").get(0))),
                        empl.getDocument()
                );

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

    private static class DeleteHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                    httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
            try {
                String response = "was deleted tuple";
                int status = 200;

                MongoClient mongoClient = new MongoClient();
                MongoDatabase mongoDatabase = mongoClient.getDatabase("lab6pad");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("pad");

                mongoCollection.deleteOne(
                        new BsonDocument("firstname", new BsonString(httpExchange.getRequestHeaders().get("firstname").get(0)))
                );

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

    private static class GetHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                    httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
            try {
                Headers h = httpExchange.getResponseHeaders();
                h.add("Content-Type", "application/json");

                MongoClient mongoClient = new MongoClient();
                MongoDatabase mongoDatabase = mongoClient.getDatabase("lab6pad");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("pad");

                FindIterable<Document> datas = mongoCollection.find();
                List<Empl> empls = new ArrayList<>();
                datas.forEach((Block<? super Document>) document -> {
                    Empl empl = new Empl(document);
                    empls.add(empl);
                });

                String rns = JSONUtil.getJSONStringfromJAVAObject(empls);
                httpExchange.sendResponseHeaders(200, rns.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(rns.getBytes(), 0, rns.length());
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

    private static class GetByOffsetAndLimitHandler implements HttpHandler {
        public void handle(HttpExchange httpExchange) {
            LOGGER.info(format("[SERVER] --> Somebody access services: remote address = %s, request method = %s, request uri = %s",
                    httpExchange.getRemoteAddress(), httpExchange.getRequestMethod(), httpExchange.getRequestURI()));
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

                MongoClient mongoClient = new MongoClient();
                MongoDatabase mongoDatabase = mongoClient.getDatabase("lab6pad");
                MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("pad");

                FindIterable<Document> datas = mongoCollection.find().skip(values.get("offset")).limit(values.get("limit"));
                List<Empl> empls = new ArrayList<>();
                datas.forEach((Block<? super Document>) document -> {
                    Empl empl = new Empl(document);
                    empls.add(empl);
                });

                String rns = JSONUtil.getJSONStringfromJAVAObject(empls);
                httpExchange.sendResponseHeaders(200, rns.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(rns.getBytes(), 0, rns.length());
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


}