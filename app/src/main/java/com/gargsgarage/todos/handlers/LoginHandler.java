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
public class LoginHandler implements HttpHandler {

    /**
     *
     */
    private final TodoDB todos;
    private final UsersDB users;

    /**
     * @param todos
     */
    public LoginHandler(TodoDB todos, UsersDB users) {
        this.todos = todos;
        this.users = users;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        //get the page html if a get request is sent
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            String body = TodoListView.getLoginPageHtml(true);

            exchange.sendResponseHeaders(200, body.length());
            exchange.getResponseBody().write(body.getBytes());

            return;
        }
        else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            Map<String, String> params = RequestUtils.getFormParams(exchange.getRequestBody());

            String requestMethod = params.get("action");
            String body;
            if(requestMethod.equalsIgnoreCase("Login")){
                String email = params.get("email");
                String password = params.get("password");

                //if invalid login combo
                if(!users.checkValidLogin(email, password)){
                    //make user try again
                    body = TodoListView.getLoginPageHtml(false);

                    exchange.sendResponseHeaders(200, body.length());
                    exchange.getResponseBody().write(body.getBytes());  
                }
                else{
                    //if successful login
                    User user = users.getUser(email);
                    body = TodoListView.getTodosPageHtml(todos, user);
                    String userID = Integer.valueOf(user.getID()).toString();

                    // send redirect to /todos
                    exchange.getResponseHeaders().set("Location", "/todos");
                    exchange.getResponseHeaders().add("Set-Cookie", "userID=" + userID + "; Max-Age=3600");
                    exchange.sendResponseHeaders(302, -1); // 302 Found redirect status code
                }
            }
            //if the "Don't have an account?" button is clicked
            else{
                body = TodoListView.getRegisterPageHtml(false);

                // send redirect to /todos/register
                exchange.getResponseHeaders().set("Location", "/todos/register");
                exchange.sendResponseHeaders(302, -1); // 302 Found redirect status code
            }
        }
        return;
    }
}
