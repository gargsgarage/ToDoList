package com.gargsgarage.todos.handlers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.gargsgarage.todos.model.TodoDB;
import com.gargsgarage.todos.model.User;
import com.gargsgarage.todos.model.Task;
import com.gargsgarage.todos.model.UsersDB;
import com.gargsgarage.todos.utils.RequestUtils;
import com.gargsgarage.todos.view.TodoListView;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

// handler for /todos
public class CreateTaskHandler implements HttpHandler {

    /**
     *
     */
    private final TodoDB todos;
    private final UsersDB users;

    /**
     * @param todos
     */
    public CreateTaskHandler(TodoDB todos, UsersDB users) {
        this.todos = todos;
        this.users = users;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // figure out which type of request needs to be handled (GET vs POST)
        String requestMethod = exchange.getRequestMethod();

        // check if request method is post (dont need to check for get
        // request because get request will just execute the code at the bottom
        // anyways)
        // if a post request, then just need to add user inputed task to the tasklist

        //get the userID out of the cookie header
        //if no cookie found, redirect to login page
        if (!isAuthenticated(exchange)) {
            exchange.getResponseHeaders().set("Location", "/todos/login");
            exchange.sendResponseHeaders(302, -1); // 302 Found redirect status code
            return;
        }

        List<String> cookieVals = exchange.getRequestHeaders().get("Cookie");
        String cookie = cookieVals.get(0);
        String[] cookieParams = cookie.split(";");
        cookie = cookieParams[0].substring(cookieParams[0].indexOf("=")+1);

        int userID = Integer.parseInt(cookie);
        User user = users.getUser(userID);

        if (requestMethod.equalsIgnoreCase("POST")) {

            // scan the user input
            Map<String, String> params = RequestUtils.getFormParams(exchange.getRequestBody());

            //check if the user hit the logout button or the add task button
            String rqMethod = params.get("action");

            if(rqMethod.equalsIgnoreCase("Log out")){
                // send redirect to /todos and make sure cookie header is cleared
                exchange.getResponseHeaders().add("Set-Cookie", "userID=; Max-Age=0; Path=/; HttpOnly");

                exchange.getResponseHeaders().set("Location", "/todos/login");
                exchange.sendResponseHeaders(302, -1); // 302 Found redirect status code
            }

            
            String title = params.get("taskTitle");

            if (!title.isEmpty()) {
                Task t = new Task(title, Task.Status.NOT_STARTED, userID);
                todos.addTask(t);
            }

        }
        // get the html for the handled request
        String body = TodoListView.getTodosPageHtml(todos, user);

        // make sure the response code is right based on the type of request made
        int rCode = 200;
        if (requestMethod.equalsIgnoreCase("POST")) {
            rCode++;
        }

        // send the response headers
        exchange.sendResponseHeaders(rCode, body.length());
        // send the response body
        exchange.getResponseBody().write(body.getBytes());

    }

    private boolean isAuthenticated(HttpExchange exchange) {
        List<String> cookies = exchange.getRequestHeaders().get("Cookie");
        if (!cookies.isEmpty() || cookies != null) {
            for (String cookie : cookies) {
                if (cookie.contains("userID=")) {
                    // Perform additional validation if necessary
                    return true; // Assuming the presence of 'userID' means authenticated
                }
            }
        }
        return false;
    }

}