package com.gargsgarage.todos.view;

import com.gargsgarage.todos.model.TODOList;
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

                <label><input type="radio" name="status" value="NOT_STARTED"> Not Started</label><br>
                <label><input type="radio" name="status" value="IN_PROGRESS"> In Progress</label><br>
                <label><input type="radio" name="status" value="COMPLETED"> Completed</label><br>

                <input type="submit" name="action" value="Update Title" /><br>
                <input type="submit" name="action" value="Delete Task" />
            </form>
            """;

    private static final String HTML_END = """
            </body>
            </html>
            """;

    // gets all of the html for the /todos page into one string
    public static String getTodosPageHtml(TODOList todos) {

        StringBuffer output = new StringBuffer();
        output.append(HTML_ADD_TASK_HEADER);
        output.append(getTodosAsHtml(todos));
        output.append("</br>");
        output.append(HTML_ADD_TASK_FORM);
        output.append(HTML_END);

        return output.toString();
    }

    // gets the html for writing out the list of task needed to do
    public static String getTodosAsHtml(TODOList todos) {
        StringBuffer output = new StringBuffer();
        // if there are no tasks to print, print list is empty
        if (todos.isEmpty()) {
            output.append("<p>list is empty</p></br>");
        }
        // else loop through todos and print each task in the list
        else {
            output.append("<ul>");
            for (int i = 0; i < todos.size(); i++) {
                Task task = todos.getTask(i);
                int taskID = todos.getTaskID(task);

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
    public static String getUpdatePageHtml(int id, String title) {
        StringBuffer output = new StringBuffer();
        output.append(HTML_UPDATE_TASK_HEADER);
        output.append("</br>");
        output.append(String.format(HTML_UPDATE_TASK_FORM, id, title));
        output.append(HTML_END);

        return output.toString();
    }

}
