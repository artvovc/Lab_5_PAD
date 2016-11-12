package com.controllers;

import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;

abstract class AbstractServerController {
    void sendRNS(HttpExchange httpExchange, String response, int status) throws Exception {
        httpExchange.sendResponseHeaders(status, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
