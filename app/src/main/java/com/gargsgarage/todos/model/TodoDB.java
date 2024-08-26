package com.gargsgarage.todos.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TodoDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/todolist";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private Connection con;

    public TodoDB(){
        try{
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public Connection getConnection(){
        return con;
    }

    public void closeConnection(){
        if(con != null){
            try{
                con.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void addTask(Task task){
        String taskName = task.getTitle();
        String status = task.getStatus().toString();
        int userID = task.getUserID();
        
        String query = "INSERT INTO tasks (task_name, status, user_id) VALUES (?, ?, ?)";

        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setString(1, taskName);
            statement.setString(2, status);
            statement.setInt(3, userID);
            statement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void removeTask(Task task){
        String taskName = task.getTitle();
        String status = task.getStatus().toString();

        String query = "DELETE FROM tasks WHERE task_name = ? AND status = ?";

        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setString(1, taskName);
            statement.setString(2, status);
            statement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public int getTaskID(Task task){
        String taskName = task.getTitle();
        String status = task.getStatus().toString();

        String query = "SELECT id FROM tasks WHERE task_name = ? AND status = ?";

        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setString(1, taskName);
            try(ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    int id = rs.getInt("id");
                    return id;
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return -1;
    }

    public int getNumTasks(){
        String query = "SELECT COUNT(*) AS task_count FROM tasks";

        try(PreparedStatement statement = con.prepareStatement(query);
         ResultSet rs = statement.executeQuery()){
            if(rs.next()){
                return rs.getInt("task_count");
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return 0;
    }

    public Task getTask(String taskName){
        String query = "SELECT id, status, user_id FROM tasks WHERE task_name = ?";
        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setString(1, taskName);
            try(ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String status = rs.getString("status");
                    int userID = rs.getInt("user_id");
                    return new Task(id, taskName, status, userID);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Task getTask(int id){
        String query = "SELECT task_name, status, user_id FROM tasks WHERE id = ?";
        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setInt(1, id);
            try(ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    String taskName = rs.getString("task_name");
                    String status = rs.getString("status");
                    int userID = rs.getInt("user_id");
                    return new Task(id, taskName, status, userID);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public List<Task> getAllTasks(){
        String query = "SELECT * from tasks ORDER BY id";
        List<Task> tasks = new ArrayList<Task>();
        
        try(PreparedStatement statement = con.prepareStatement(query)){
            try(ResultSet rs = statement.executeQuery()){
                while(rs.next()){
                    String taskName = rs.getString("task_name");
                    String status = rs.getString("status");
                    int id = rs.getInt("id");
                    int userID = rs.getInt("user_id");
                    Task task = new Task(id, taskName, status, userID);
                    tasks.add(task);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return tasks;
    }

    public List<Task> getUsersTasks(User user){
        int userID = user.getID();
        String query = "SELECT * from tasks WHERE user_id = ? ORDER BY id";
        List<Task> tasks = new ArrayList<Task>();
        
        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setInt(1, userID);
            try(ResultSet rs = statement.executeQuery()){
                while(rs.next()){
                    String taskName = rs.getString("task_name");
                    String status = rs.getString("status");
                    int id = rs.getInt("id");
                    Task task = new Task(id, taskName, status, userID);
                    tasks.add(task);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return tasks;
    }

    public void updateTaskName(int id, String newName){
        String query = "UPDATE tasks SET task_name = ? WHERE id = ?";

        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setString(1, newName);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void updateTaskStatus(int id, String newStatus){
        String query = "UPDATE tasks SET status = ? WHERE id = ?";

        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setString(1, newStatus);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
