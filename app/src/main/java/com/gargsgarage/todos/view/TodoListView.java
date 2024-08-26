package com.gargsgarage.todos.view;

import com.gargsgarage.todos.model.TodoDB;
import com.gargsgarage.todos.model.User;
import com.gargsgarage.todos.model.Task;

public class TodoListView {

    private static final String HTML_ADD_TASK_HEADER = 
        "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "    <meta charset=\"UTF-8\">\n" +
        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
        "    <title>Add Task</title>\n" +
        "</head>\n" +
        "<body>\n";


    private static final String HTML_UPDATE_TASK_HEADER =
        "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "    <meta charset=\"UTF-8\">\n" +
        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
        "    <title>Update Task</title>\n" +
        "</head>\n" +
        "<body>\n";


    private static final String HTML_ADD_TASK_FORM =
        "<h1>Add Task</h1>\n" +
        "<form method=\"post\" action=\"/todos\">\n" +
        "    <label for=\"taskTitle\">Task Title:</label><br>\n" +
        "    <input type=\"text\" id=\"taskTitle\" name=\"taskTitle\" /><br>\n" +
        "    <input type=\"submit\" name=\"action\" value=\"Add Task\" /><br>\n" +
        "    <input type=\"submit\" name=\"action\" value=\"Log out\" />\n" +
        "</form>\n";


    private static final String HTML_UPDATE_TASK_FORM =
        "<h1>Update Task Title</h1>\n" +
        "<form method=\"post\" action=\"/todos/update\">\n" +
        "    <input type=\"hidden\" name=\"id\" value=\"%d\" />\n" +
        "    <label for=\"taskTitle\">New Task Title:</label><br>\n" +
        "    <input type=\"text\" id=\"taskTitle\" name=\"taskTitle\" value=\"%s\" /><br>\n";


    private static final String HTML_UPDATE_BUTTONS =
        "    <input type=\"submit\" name=\"action\" value=\"Update Task\" /><br>\n" +
        "    <input type=\"submit\" name=\"action\" value=\"Delete Task\" />\n" +
        "</form>\n";

    
    private static final String HTML_END =
        "</body>\n" +
        "</html>\n";


    private static final String HTML_LOGIN_HEADER =
        "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "    <meta charset=\"UTF-8\">\n" +
        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
        "    <title>Login</title>\n" +
        "</head>\n" +
        "<body>\n";


    private static final String HTML_LOGIN_FORM =
        "<h1>Login</h1>\n" +
        "<form method=\"post\" action=\"/todos/login\">\n" +
        "    <label for=\"email\">Email:</label><br>\n" +
        "    <input type=\"email\" id=\"email\" name=\"email\"><br>\n" +
        "\n" +
        "    <label for=\"password\">Password:</label><br>\n" +
        "    <input type=\"text\" id=\"password\" name=\"password\"><br>\n" +
        "\n" +
        "    <input type=\"submit\" name=\"action\" value=\"Login\" /><br>\n" +
        "    <input type=\"submit\" name=\"action\" value=\"Don't have an account?\" />\n" +
        "</form>\n" +
        "</body>\n" +
        "</html>\n";

    

    private static final String HTML_REGISTER_ACCT_HEADER =
        "<!DOCTYPE html>\n" +
        "<html lang=\"en\">\n" +
        "<head>\n" +
        "    <meta charset=\"UTF-8\">\n" +
        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
        "    <title>Create an Account</title>\n" +
        "</head>\n" +
        "<body>\n";


    private static final String HTML_REGISTER_ACCT_FORM =
        "<form method=\"post\" action=\"/todos/register\">\n" +
        "    <h1>Create an Account</h1>\n" +
        "\n" +
        "    <label for=\"email\">Email:</label><br>\n" +
        "    <input type=\"email\" id=\"email\" name=\"email\"><br>\n" +
        "\n" +
        "    <label for=\"name\">Name:</label><br>\n" +
        "    <input type=\"text\" id=\"name\" name=\"name\"><br>\n" +
        "\n" +
        "    <label for=\"password\">Password:</label><br>\n" +
        "    <input type=\"text\" id=\"password\" name=\"password\"><br>\n" +
        "\n" +
        "    <input type=\"submit\" name=\"action\" value=\"Create Account\" /><br>\n" +
        "    <input type=\"submit\" name=\"action\" value=\"Already have an account?\" />\n" +
        "</form>\n" +
        "</body>\n" +
        "</html>\n";


    public static String getLoginPageHtml(boolean validLogin){
        String output = HTML_LOGIN_HEADER;
        if(!validLogin){
            output+= "<p style=\"color: red;\">invalid email or password</p>";
        }
        output += HTML_LOGIN_FORM;
        return output;
    }

    public static String getRegisterPageHtml(boolean isDuplicateEmail){
        String output = HTML_REGISTER_ACCT_HEADER;
        if(isDuplicateEmail){
            output+= "<p style=\"color: red;\">account already exists for this email</p>";
        }
        output += HTML_REGISTER_ACCT_FORM;
        return output;
    }

    // gets all of the html for the /todos page into one string
    public static String getTodosPageHtml(TodoDB todos, User user) {

        StringBuffer output = new StringBuffer();
        output.append(HTML_ADD_TASK_HEADER);
        output.append("<h1>Hello, " + user.getName() + "</h1>");
        output.append(getTodosAsHtml(todos, user));
        output.append("</br>");
        output.append(HTML_ADD_TASK_FORM);
        output.append(HTML_END);

        return output.toString();
    }

    // gets the html for writing out the list of task needed to do
    public static String getTodosAsHtml(TodoDB todos, User user) {
        StringBuffer output = new StringBuffer();
        // if there are no tasks to print, print list is empty
        if (todos.getNumTasks() == 0) {
            output.append("<p>list is empty</p></br>");
        }
        // else loop through todos and print each task in the list
        else {
            output.append("<ul>");
            for (Task task:todos.getUsersTasks(user)) {
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
