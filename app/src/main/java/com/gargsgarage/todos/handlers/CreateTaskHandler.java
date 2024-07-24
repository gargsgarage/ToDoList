package com.gargsgarage.todos.handlers;

import java.io.IOException;
import java.util.Map;

import com.gargsgarage.todos.model.TodoDB;
import com.gargsgarage.todos.model.Task;
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

    /**
     * @param todos
     */
    public CreateTaskHandler(TodoDB todos) {
        this.todos = todos;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // figure out which type of request needs to be handled
        String requestMethod = exchange.getRequestMethod();

        // check if request method is post (dont need to check for get
        // request because get request will just execute the code at the bottom
        // anyways)
        // if a post request, then just need to add user inputed task to the tasklist
        if (requestMethod.equalsIgnoreCase("POST")) {

            // scan the user input
            Map<String, String> params = RequestUtils.getFormParams(exchange.getRequestBody());
            String title = params.get("taskTitle");

            if (!title.isEmpty()) {
                Task t = new Task(title, Task.Status.NOT_STARTED);
                todos.addTask(t);
            }

        }
        // get the html for the handled request
        String body = TodoListView.getTodosPageHtml(todos);

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

}