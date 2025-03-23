package com.example.greenpulse.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Field {
    public String title;
    public String description;
    public List<MyLatLng>points;
    public List<Task>tasks;

    public Field() {
    }

    public Field(String title, String description, List<MyLatLng> points, List<Task> tasks) {
        this.title = title;
        this.description = description;
        this.points = points;
        this.tasks = tasks;
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

    public List<MyLatLng> getPoints() {
        return points;
    }

    public void setPoints(List<MyLatLng> points) {
        this.points = points;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
