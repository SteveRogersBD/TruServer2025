package com.example.greenpulse.models;

public class Event {
    public String title;
    public String description;
    public Double lat;
    public Double lon;

    public Event(String title, String description, Double lat, Double lon) {
        this.title = title;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
    }
}
