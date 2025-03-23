package com.example.greenpulse.models;

import java.time.LocalDateTime;
import java.util.Date;

public class Task {
    String title;
    String description;
    boolean isDone;
    Date createdAt;

    public Task() {
    }

    public Task(String title, String description, boolean isDone, Date createdAt) {
        this.title = title;
        this.description = description;
        this.isDone = isDone;
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
