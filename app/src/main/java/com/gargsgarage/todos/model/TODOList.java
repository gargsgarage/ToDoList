package com.gargsgarage.todos.model;

import java.util.*;

public class TODOList {
    private ArrayList<Task> tasks;

    public TODOList(){
        tasks = new ArrayList<Task>();
    }

    public void addTask(Task t){
        tasks.add(t);
    }
    
    public void removeTask(Task t){
        for(int i = tasks.size() - 1; i >= 0; i--){
            if(t.compareTo(tasks.get(i)) == 0){
                tasks.remove(tasks.get(i));
            }
        }
    }

    public Task getTask(String title){
        for(int i = 0; i < tasks.size(); i++){
            if(tasks.get(i).getTitle().compareTo(title) == 0){
                return tasks.get(i);
            }
        }
        return null;
    }

    public Task getTask(int index){
        return tasks.get(index);
    }

    public List<Task> getAllTasks(){
        return tasks;
    }

    public String toString(){
        String result = "";
        for(int i = 0; i < tasks.size();i++){
            result += tasks.get(i).toString() + ", ";
        }
        return result;
    }

    public boolean isEmpty(){
        if(tasks.size() == 0){
            return true;
        }
        return false;
    }

    public int size(){
        return tasks.size();
    }

    //returns task id (0 thru tasks.size() - 1)
    public int getTaskID(Task t){
        int i = 0;
        while(t.compareTo(tasks.get(i)) != 0){
            i++;
        }
        return i;
    }
    
}
