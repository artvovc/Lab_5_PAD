package com.http;

import com.controllers.*;
import com.sun.net.httpserver.HttpServer;
import org.apache.log4j.Logger;

import java.net.InetSocketAddress;

import static java.lang.String.format;

public class CustomHttpServer {

    private static final int PORT = 9001;

    private static final Logger LOGGER = Logger.getLogger(CustomHttpServer.class);

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/employee/post", new PostHandler());
        server.createContext("/employee/get", new GetHandler());
        server.createContext("/employee/put", new PutHandler());
        server.createContext("/employee/delete", new DeleteHandler());
        server.createContext("/employee", new GetByOffsetAndLimitHandler());
        server.createContext("/status", new StatusHandler());
        server.setExecutor(null);
        server.start();
        LOGGER.info(format("[SERVER] --> WAS STARTED SERVER ON PORT %d",PORT));
    }

}