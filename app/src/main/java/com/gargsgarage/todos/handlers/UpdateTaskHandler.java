package com.gargsgarage.todos.handlers;

import java.io.IOException;
import java.util.Map;

import com.gargsgarage.todos.model.TodoDB;
import com.gargsgarage.todos.model.Task;
import com.gargsgarage.todos.utils.RequestUtils;
import com.gargsgarage.todos.view.TodoListView;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

// handler for /todos/update
public class UpdateTaskHandler implements HttpHandler {

    /**
     *
     */
    private final TodoDB todos;

    /**
     * @param todos
     */
    public UpdateTaskHandler(TodoDB todos) {
        this.todos = todos;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // if get request, then return the update page
        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {

            // extract the id from the query
            String query = exchange.getRequestURI().getQuery();
            int taskID = extractID(query);

            // if task not found, return
            if (taskID < 0) {
                exchange.sendResponseHeaders(404, 0);
                return;
            }

            // get the task using the taskID
            Task task = todos.getTask(taskID);

            // get the body of the response
            String body = TodoListView.getUpdatePageHtml(task);

            exchange.sendResponseHeaders(200, body.length());
            exchange.getResponseBody().write(body.getBytes());

            return;
        }
        // if POST request, then update the fields of task with id = taskID, redirect to
        // /todos
        else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            // Extract the task ID from the input stream
            Map<String, String> params = RequestUtils.getFormParams(exchange.getRequestBody());
            int taskID = -1;
            try {
                taskID = Integer.parseInt(params.get("id"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            // if task not found, return
            if (taskID < 0) {
                exchange.sendResponseHeaders(404, 0);
                return;
            }

            // get a reference to the correct task using taskID
            Task task = todos.getTask(taskID);

            //since html forms typically only handles PUT and POST requests,
            // to make delete button work, we must override a post request
            // --> done by having form also have input type(rqmethod) param
            String requestMethod = params.get("action");
            if (!requestMethod.isBlank() && !requestMethod.equalsIgnoreCase("Delete Task")) {
                //otherwise this request was a normal POST request to update the task
                // update task title
                todos.updateTaskName(taskID, params.get("taskTitle"));

                // update status
                String newStatus = params.get("status");
                todos.updateTaskStatus(taskID, newStatus);
            } else {

                todos.removeTask(task);
            }

            // send redirect to /todos
            exchange.getResponseHeaders().set("Location", "/todos");
            exchange.sendResponseHeaders(302, -1); // 302 Found redirect status code

        }
    }

    // extracts the id of the task from the query
    public int extractID(String query) {
        int taskID = -1;
        if (query != null && query.startsWith("id=")) {
            // will get the id in the form of a string from the query
            String taskIDStr = query.substring(3);
            try {
                taskID = Integer.parseInt(taskIDStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return taskID;
    }

}