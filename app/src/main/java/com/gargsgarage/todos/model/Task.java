package com.gargsgarage.todos.model;

public class Task {
    private String title;
    private String description;
    private Status status;
    private int id;
    private static int previousID = 1;
    private int userID;
    public enum Status{
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED;
    }
    
    public Task(int id, String title, String status, int userID){
        this.id = id;
        previousID = id;
        this.title = title;
        if (status == null) {
            this.status = Status.NOT_STARTED;
        } else{
            for(Status s: Status.values()){
                if(status != null && s.toString().equals(status)){
                    this.status = s;
                    break;
                }
            }    
        }
    }

    public Task(String title, Status status, int userID){
        this.title = title;
        this.status = status;
        id = previousID + 1;
        previousID = id;
        this.userID = userID;
    }

    public int getUserID(){
        return userID;
    }
    
    public String getTitle(){
        return title;
    }

    public int getID(){
        return id;
    }

    public String getDescription(){
        return description;
    }

    public Status getStatus(){
        return status;
    }

    public void setTitle(String t){
        title = t;
    }

    public void setDescription(String d){
        description = d;
    }

    public void setStatus(Status s){
        status = s;
    }

    public int compareTo(Task t){
        if(t == null){
            throw new IllegalArgumentException("param must not be null");
        }
        if(this.getTitle().equals(t.getTitle())){
            if(this.getStatus().equals(t.getStatus())){
                return 0;
            }
            return this.getStatus().compareTo(t.getStatus());
        }
        return this.getTitle().compareTo(t.getTitle());
    }

    @SuppressWarnings("static-access")
    public String toString(){
        String result = "Title: " + title;
        
        if(description != null){
            result += ", description: " + description; 
        }
        
        result += ", status: ";

        if(status == status.NOT_STARTED){
            result += "NOT STARTED";
        }
        else if(status == status.IN_PROGRESS){
            result += "IN PROGRESS";
        }
        else{
            result += "COMPLETED";
        }
        return result;
    }
}
