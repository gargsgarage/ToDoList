package org.example;

public class Task {
    private String title;
    private String description;
    private status status;
    private enum status{
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETE;
    }
    

    public Task(String title, String description, status status){
        this.title = title;
        this.description = description;
        this.status = status;
    }

    public Task(String title, status status){
        this.title = title;
        this.status = status;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public status getStatus(){
        return status;
    }

    public void setTitle(String t){
        title = t;
    }

    public void setDescription(String d){
        description = d;
    }

    public void setStatus(status s){
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
