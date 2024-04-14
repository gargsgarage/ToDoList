/*
 * This source file was generated by the Gradle 'init' task
 */
package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class TODOServer {

    TODOList todos;

    private final String HTML_ADD_TASK_HEADER = """
            <!DOCTYPE html>
            <html lang=\"en\">
            <head>
                <meta charset=\"UTF-8\">
                <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
                <title>Add Task</title>
            </head>
            <body>
            """;

    private final String HTML_UPDATE_TASK_HEADER = """
            <!DOCTYPE html>
            <html lang=\"en\">
            <head>
                <meta charset=\"UTF-8\">
                <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
                <title>Update Task</title>
            </head>
            <body>
            """;

    private final String HTML_ADD_TASK_FORM = """
            <h1>Add Task</h1>
            <form method="post" action="/todos">
                <label for="taskTitle">Task Title:</label><br>
                <input type="text" id="taskTitle" name="taskTitle" /><br>
                <input type="submit" value="Add Task" />
            </form>
            """;

    private final String HTML_UPDATE_TASK_FORM = """
            <h1>Update Task Title</h1>
            <form method="post" action="/todos/update">
                <input type="hidden" name="id" value="%d" />
                <label for="taskTitle">New Task Title:</label><br>
                <input type="text" id="taskTitle" name="taskTitle" value="%s" /><br>

                <label><input type="radio" name="status" value="NOT_STARTED"> Not Started</label><br>
                <label><input type="radio" name="status" value="IN_PROGRESS"> In Progress</label><br>
                <label><input type="radio" name="status" value="COMPLETED"> Completed</label><br>

                <input type="submit" value="Update Title" />
            </form>
            """;

    private final String HTML_END = """
            </body>
            </html>
            """;

    public TODOServer() {
        todos = new TODOList();
    }

    public static void main(String[] args) {
        // create the server
        TODOServer server = new TODOServer();
        // link it to an HTTP server
        server.startTODOListService();
    }

    //starts up the todo service
    public void startTODOListService() {
        try {
            // create HTTP server
            HttpServer httpServer = HttpServer.create();
            // set up a path to handle requests and call the handler
            // associated with it to generate the response
            httpServer.createContext("/todos", new TODOListHandler());
            httpServer.createContext("/todos/update", new UpdateListHandler());
            // tell the server to see what port to listen to
            httpServer.bind(new InetSocketAddress(8000), 0);

            httpServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //gets all of the html for the /todos page into one string 
    public String getTodosPageHtml() {

        StringBuffer output = new StringBuffer();
        output.append(HTML_ADD_TASK_HEADER);
        output.append(getTodosAsHtml());
        output.append("</br>");
        output.append(HTML_ADD_TASK_FORM);
        output.append(HTML_END);

        return output.toString();
    }

    //gets the html for writing out the list of task needed to do
    public String getTodosAsHtml() {
        StringBuffer output = new StringBuffer();
        //if there are no tasks to print, print list is empty
        if (todos.isEmpty()) {
            output.append("<p>list is empty</p></br>");
        }
        //else loop through todos and print each task in the list
        else {
            output.append("<ul>");
            for (int i = 0; i < todos.size(); i++) {
                Task task = todos.getTask(i);
                int taskID = todos.getTaskID(task);

                output.append("<li>");

                //print each task as a link (which will be used to update the task)
                output.append("<a href=\"/todos/update?id=").append(taskID).append("\">");
                output.append(task.toString());
                output.append("</a>");

                output.append("</li>");
            }
            output.append("</ul>");
        }

        return output.toString();
    }

    //gets all of the html for the /todos/update page into one string 
    // (params allow previous params to be apart of the page)
    // ex. if the title is currently hello, update will show:
    // update Title: hello, and then you can edit that
    public String getUpdatePageHtml(int id, String title) {
        StringBuffer output = new StringBuffer();
        output.append(HTML_UPDATE_TASK_HEADER);
        output.append("</br>");
        output.append(String.format(HTML_UPDATE_TASK_FORM, id, title));
        output.append(HTML_END);

        return output.toString();
    }


    //parses through the input stream to create key-->value pairs for each of the fields of a task
    private Map<String, String> getFormParams(InputStream inputStream) {

        // first get the request body, which is the user's input
        // still need to split and decode the body
        Scanner sc = new Scanner(inputStream);
        String requestBody;

        if (sc.hasNext()) {
            requestBody = sc.useDelimiter("\\A").next();
        } else {
            requestBody = "";
        }
        sc.close();

        // split and decode the pairs
        // (ex current requestBody: id=0&taskTitle=say+hello&status=IN_PROGRESS)
        String[] pairs = requestBody.toString().split("&");
        Map<String, String> params = new HashMap<>();
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
            }
        }
        // after splitting and decoding, params Map looks like this:
        // params: id-->0, taskTitle-->say hello, status--> IN_PROGRESS

        return params;

    }

    //handler for /todos
    public class TODOListHandler implements HttpHandler {

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
                Map<String, String> params = getFormParams(exchange.getRequestBody());
                String title = params.get("taskTitle");

                if (!title.isEmpty()) {
                    Task t = new Task(title, Task.Status.NOT_STARTED);
                    todos.addTask(t);
                }

            }
            // get the html for the handled request
            String body = getTodosPageHtml();

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

    //handler for /todos/update
    public class UpdateListHandler implements HttpHandler {

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
                String body = getUpdatePageHtml(taskID, task.getTitle());

                exchange.sendResponseHeaders(200, body.length());
                exchange.getResponseBody().write(body.getBytes());

                return;
            }
            //if POST request, then update the fields of task with id = taskID, redirect to /todos
            else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                // Extract the task ID from the input stream
                Map<String, String> params = getFormParams(exchange.getRequestBody());
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

                // update task title
                task.setTitle(params.get("taskTitle"));

                // update status
                Task.Status newStatus = Task.Status.valueOf(params.get("status"));
                task.setStatus(newStatus);

                // send redirect to /todos
                exchange.getResponseHeaders().set("Location", "/todos");
                exchange.sendResponseHeaders(302, -1); // 302 Found redirect status code

            }
        }

        //extracts the id of the task from the query
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

}
