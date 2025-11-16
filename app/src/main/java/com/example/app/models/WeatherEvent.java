package com.example.app.models;

import java.util.Date;

public class WeatherEvent {
    private String title;
    private String description;
    private Date date;
    private String time;
    private String eventType; // "reminder", "alert", "custom"
    private boolean isCompleted;

    public WeatherEvent(String title, String description, Date date, String time) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.eventType = "custom";
        this.isCompleted = false;
    }

    public WeatherEvent(String title, String description, Date date, String time, String eventType) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.eventType = eventType;
        this.isCompleted = false;
    }

    // Getters and Setters
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
