package com.gargsgarage.todos.view;

import com.gargsgarage.todos.model.TodoDB;
import com.gargsgarage.todos.model.Task;

public class TodoListView {

    private static final String HTML_ADD_TASK_HEADER = """
            <!DOCTYPE html>
            <html lang=\"en\">
            <head>
                <meta charset=\"UTF-8\">
                <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
                <title>Add Task</title>
            </head>
            <body>
            """;

    private static final String HTML_UPDATE_TASK_HEADER = """
            <!DOCTYPE html>
            <html lang=\"en\">
            <head>
                <meta charset=\"UTF-8\">
                <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">
                <title>Update Task</title>
            </head>
            <body>
            """;

    private static final String HTML_ADD_TASK_FORM = """
            <h1>Add Task</h1>
            <form method="post" action="/todos">
                <label for="taskTitle">Task Title:</label><br>
                <input type="text" id="taskTitle" name="taskTitle" /><br>
                <input type="submit" value="Add Task" />
            </form>
            """;

    private static final String HTML_UPDATE_TASK_FORM = """
            <h1>Update Task Title</h1>
            <form method="post" action="/todos/update">
                <input type="hidden" name="id" value="%d" />
                <label for="taskTitle">New Task Title:</label><br>
                <input type="text" id="taskTitle" name="taskTitle" value="%s" /><br>

            """;

    private static final String HTML_UPDATE_BUTTONS = """
                <input type="submit" name="action" value="Update Task" /><br>
                <input type="submit" name="action" value="Delete Task" />
            </form>
            """;
    
    private static final String HTML_END = """
            </body>
            </html>
            """;

    // gets all of the html for the /todos page into one string
    public static String getTodosPageHtml(TodoDB todos) {

        StringBuffer output = new StringBuffer();
        output.append(HTML_ADD_TASK_HEADER);
        output.append(getTodosAsHtml(todos));
        output.append("</br>");
        output.append(HTML_ADD_TASK_FORM);
        output.append(HTML_END);

        return output.toString();
    }

    // gets the html for writing out the list of task needed to do
    public static String getTodosAsHtml(TodoDB todos) {
        StringBuffer output = new StringBuffer();
        // if there are no tasks to print, print list is empty
        if (todos.getNumTasks() == 0) {
            output.append("<p>list is empty</p></br>");
        }
        // else loop through todos and print each task in the list
        else {
            output.append("<ul>");
            for (Task task:todos.getAllTasks()) {
                int taskID = task.getID();

                output.append("<li>");

                // print each task as a link (which will be used to update the task)
                output.append("<a href=\"/todos/update?id=").append(taskID).append("\">");
                output.append(task.toString());
                output.append("</a>");

                output.append("</li>");
            }
            output.append("</ul>");
        }

        return output.toString();
    }

    // gets all of the html for the /todos/update page into one string
    // (params allow previous params to be apart of the page)
    // ex. if the title is currently hello, update will show:
    // update Title: hello, and then you can edit that
    public static String getUpdatePageHtml(Task t) {
        int id = t.getID();
        String title = t.getTitle();
        StringBuffer output = new StringBuffer();
        output.append(HTML_UPDATE_TASK_HEADER);
        output.append("</br>");
        output.append(String.format(HTML_UPDATE_TASK_FORM, id, title));
        output.append(getUpdatedTaskStatusHtml(t));
        output.append(HTML_UPDATE_BUTTONS);
        output.append(HTML_END);

        return output.toString();
    }

    public static String getUpdatedTaskStatusHtml(Task t){
        String output = "<label><input type=\"radio\" name=\"status\" value=\"NOT_STARTED\"";
        if (t.getStatus() == Task.Status.NOT_STARTED){
            output += " checked";
        }
        output += "> Not Started</label><br>";
        output += "<label><input type=\"radio\" name=\"status\" value=\"IN_PROGRESS\"";
        if (t.getStatus() == Task.Status.IN_PROGRESS){
            output += " checked";
        }
        output += "> In Progress</label><br>";
        output += "<label><input type=\"radio\" name=\"status\" value=\"COMPLETED\"";
        if (t.getStatus() == Task.Status.COMPLETED){
            output += " checked";
        }
        output += "> Completed</label><br>";

        return output;
    }
}
