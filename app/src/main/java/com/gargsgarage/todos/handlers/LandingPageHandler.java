package com.gargsgarage.todos.handlers;

import java.io.IOException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LandingPageHandler implements HttpHandler{

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String redirectTo = "/todos/login";
        exchange.getResponseHeaders().set("Location", redirectTo);
        exchange.sendResponseHeaders(302, -1); // 302 Found
    }
}
