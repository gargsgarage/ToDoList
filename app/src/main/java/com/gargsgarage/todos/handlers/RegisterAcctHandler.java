package com.gargsgarage.todos.handlers;

import com.gargsgarage.todos.model.TodoDB;
import com.gargsgarage.todos.model.User;
import com.gargsgarage.todos.model.UsersDB;

import java.io.IOException;
import java.util.Map;

import com.gargsgarage.todos.utils.RequestUtils;
import com.gargsgarage.todos.view.TodoListView;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

// handler for /todos/update
public class RegisterAcctHandler implements HttpHandler {

    /**
     *
     */
    private final TodoDB todos;
    private final UsersDB users;

    /**
     * @param todos
     */
    public RegisterAcctHandler(TodoDB todos, UsersDB users) {
        this.todos = todos;
        this.users = users;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        //get the page html if a get request is sent
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            String body = TodoListView.getRegisterPageHtml(false);
            
            exchange.sendResponseHeaders(200, body.length());
            exchange.getResponseBody().write(body.getBytes());
            return;
        }
        else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            Map<String, String> params = RequestUtils.getFormParams(exchange.getRequestBody());

            String email = params.get("email");
            String name = params.get("name");
            String password = params.get("password");

            //if the user already has an account
            String requestMethod = params.get("action");
            if(requestMethod.equalsIgnoreCase("Already have an account?")){
                 // send redirect to /todos/login to login
                exchange.getResponseHeaders().set("Location", "/todos/login");
                exchange.sendResponseHeaders(302, -1); // 302 Found redirect status code
            }

            User newUser = new User(email, name, password);

            if(users.emailExists(newUser)){
                String body = TodoListView.getRegisterPageHtml(true);
            
                exchange.sendResponseHeaders(200, body.length());
                exchange.getResponseBody().write(body.getBytes());
            }
            else{
                users.addUser(newUser);
            
                // send redirect to /todos/login to login
                exchange.getResponseHeaders().set("Location", "/todos/login");
                exchange.sendResponseHeaders(302, -1); // 302 Found redirect status code
            }
        }
    }
}
