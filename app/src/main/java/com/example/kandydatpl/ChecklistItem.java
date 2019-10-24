package com.example.kandydatpl;

import java.util.Date;

public class ChecklistItem {

    private static int id;
    private String title;
    private String description;
    private boolean userCrated;
    private boolean done;
    private Date deadline;

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        ChecklistItem.id = id;
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

    public boolean isUserCrated() {
        return userCrated;
    }

    public void setUserCrated(boolean userCrated) {
        this.userCrated = userCrated;
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
