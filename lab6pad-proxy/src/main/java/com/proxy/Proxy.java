package com.proxy;

import com.controllers.*;
import com.sun.net.httpserver.HttpServer;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

import static java.lang.String.format;

public class Proxy {

    private static final int PORT = 9002;

    private static final Logger LOGGER = Logger.getLogger(Proxy.class);

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/employee/post", new PostProxyHandler());
        server.createContext("/employee/get", new GetProxyHandler());
        server.createContext("/employee/put", new PutProxyHandler());
        server.createContext("/employee/delete", new DeleteProxyHandler());
        server.createContext("/employee", new GetProxyByOffsetAndLimitHandler());
        server.setExecutor(null);
        server.start();
        LOGGER.info(format("[PROXY] --> WAS STARTED ON PORT %d", PORT));
    }
}
