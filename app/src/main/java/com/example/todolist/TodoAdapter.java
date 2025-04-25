package com.example.todolist;

public class TodoAdapter {
    private String id;
    private String title;
    private boolean isDone;
    private boolean isPriority;

    // Constructor default diperlukan oleh Firebase
    public TodoAdapter() {
    }

    public TodoAdapter(String title, boolean isDone) {
        this.title = title;
        this.isDone = isDone;
        this.isPriority = false; // Default prioritas adalah false
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return isDone;
    }

    public boolean isPriority() {
        return isPriority;
    }

    // Setter methods
    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public void setPriority(boolean priority) {
        isPriority = priority;
    }
}
