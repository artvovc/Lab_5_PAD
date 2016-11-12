package com.controllers;

import com.operation.ReturnObject;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;

abstract class AbstractProxyController {
    void sendRNS(HttpExchange httpExchange, ReturnObject returnObject) throws Exception {
        httpExchange.sendResponseHeaders(returnObject.getStatus(), returnObject.getRns().length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(returnObject.getRns().getBytes());
        os.close();
    }
}
