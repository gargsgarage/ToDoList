/*
 * This source file was generated by the Gradle 'init' task
 */
package com.gargsgarage.todos;

import java.net.InetSocketAddress;
import com.gargsgarage.todos.handlers.CreateTaskHandler;
import com.gargsgarage.todos.handlers.UpdateTaskHandler;
import com.gargsgarage.todos.model.TODOList;
import com.sun.net.httpserver.HttpServer;

public class TODOServer {

    TODOList todos;

    public TODOServer() {
        todos = new TODOList();
    }

    public static void main(String[] args) {
        // create the server
        TODOServer server = new TODOServer();
        // link it to an HTTP server
        server.startTODOListService();
    }

    // starts up the todo service
    public void startTODOListService() {
        try {
            // create HTTP server
            HttpServer httpServer = HttpServer.create();
            // set up a path to handle requests and call the handler
            // associated with it to generate the response
            httpServer.createContext("/todos", new CreateTaskHandler(todos));
            httpServer.createContext("/todos/update", new UpdateTaskHandler(todos));
            // tell the server to see what port to listen to
            httpServer.bind(new InetSocketAddress(8000), 0);

            httpServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
