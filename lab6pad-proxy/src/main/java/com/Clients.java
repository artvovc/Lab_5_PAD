package com;


import com.sun.net.httpserver.HttpExchange;

import java.util.ArrayList;
import java.util.List;

public class Clients {
    private static Clients ourInstance = new Clients();

    public static Clients getInstance() {
        return ourInstance;
    }

    private List<HttpExchange> httpExchanges = new ArrayList<>();

    private int LIMIT = 3;

    private Clients() {
    }

    public List<HttpExchange> getHttpExchanges() {
        return httpExchanges;
    }

    public int getLIMIT() {
        return LIMIT;
    }

}
