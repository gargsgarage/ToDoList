package com.gargsgarage.todos.model;

public class User {
    private String email;
    private String name;
    //stores the HASHED password, not the plaintext password
    private String password;
    private int id;
    private static int previousID = 1;


    public User(String email, String name, String password){
        this.email = email;
        this.name = name;
        this.password = password;
        id = previousID + 1;
        previousID = id;
    }

    public User(String email, String name, String password, int id){
        this.email = email;
        this.name = name;
        this.password = password;
        this.id = id;
    }

    /*
     * IMPORTANT: ONLY TO BE USED BY UsersDB
     */
    public String getEmail(){
        return email;
    }
    
    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }

    public int getID(){
        return id;
    }
}
