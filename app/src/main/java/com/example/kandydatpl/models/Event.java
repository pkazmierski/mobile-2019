package com.example.kandydatpl.models;

import java.io.Serializable;
import java.util.Date;

public class Event implements Serializable {

    private String id;
    private String title;
    private String description;
    private boolean userCreated;
    private boolean done;
    private Date deadline;

    public Event() {
    }

    public Event(String id, String title, String description, boolean done, Date deadline) {
        this.title = title;
        this.description = description;
        this.done = done;
        this.deadline = deadline;
        this.userCreated = true;
    }

    public Event(String title, boolean done) {
        this.id = "";
        this.title = title;
        this.done = done;
        this.deadline = new Date();
    }

    public Event(String id, String title, String description, boolean userCreated, boolean done, Date deadline) {
        this.title = title;
        this.description = description;
        this.userCreated = userCreated;
        this.done = done;
        this.deadline = deadline;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isUserCreated() {
        return userCreated;
    }

    public void setUserCreated(boolean userCreated) {
        this.userCreated = userCreated;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }


}
