package com.gargsgarage.todos.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.gargsgarage.todos.utils.PasswordUtils;

public class UsersDB {
    private static final String URL = "jdbc:postgresql://localhost:5432/todolist";
    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    private Connection con;

    public UsersDB(){
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

    public void addUser(User user){
        String email = user.getEmail();
        String name = user.getName();
        String password = user.getPassword();
        String encryptedPassword = PasswordUtils.hashPassword(password);

        String query = "INSERT INTO users (email, name, password) VALUES (?, ?, ?)";
        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setString(1, email);
            statement.setString(2, name);
            statement.setString(3, encryptedPassword);

            statement.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    public boolean emailExists(User user) {
        String email = user.getEmail();
        String query = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ?)";
    
        try (PreparedStatement statement = con.prepareStatement(query)) {
            statement.setString(1, email);
    
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return false; // Default return value in case of exception
    }

    public boolean checkValidLogin(String email, String password){
        String query = "SELECT password FROM users WHERE email = ?";

        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setString(1, email);
            try(ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    String actualPassword = rs.getString("password");
                    return PasswordUtils.checkPassword(password, actualPassword);                    
                }
            }
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public User getUser(String email){
        String query = "SELECT name, id, password FROM users WHERE email = ?";

        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setString(1, email);
            try(ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    String name = rs.getString("name");
                    int id = rs.getInt("id");
                    String hashedPassword = rs.getString("password");

                    return new User(email, name, hashedPassword, id);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public User getUser(int userID){
        String query = "SELECT name, email, password FROM users WHERE id = ?";

        try(PreparedStatement statement = con.prepareStatement(query)){
            statement.setInt(1, userID);
            try(ResultSet rs = statement.executeQuery()){
                if (rs.next()) {
                    String name = rs.getString("name");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    return new User(email, name, password, userID);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
