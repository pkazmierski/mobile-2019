package com.example.kandydatpl.models;

import android.graphics.Color;

import com.github.sundeepk.compactcalendarview.domain.Event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class ChecklistEvent implements Serializable {

    private String id;
    private String title;
    private String description;
    private boolean userCreated;
    private boolean done;
    private Date deadline;
    private String studyOfferId;

    public ChecklistEvent() {
    }

    public ChecklistEvent(String id, String title, String description, boolean done, Date deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.done = done;
        this.deadline = deadline;
        this.userCreated = true;
    }

    public ChecklistEvent(String title, boolean done) {
        this.id = "";
        this.title = title;
        this.done = done;
        this.deadline = new Date();
    }

    public ChecklistEvent(String id, String title, String description, boolean userCreated, boolean done, Date deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userCreated = userCreated;
        this.done = done;
        this.deadline = deadline;
    }

    public ChecklistEvent(String id, String title, String description, boolean userCreated, boolean done, Date deadline, String studyOfferId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userCreated = userCreated;
        this.done = done;
        this.deadline = deadline;
        this.studyOfferId = studyOfferId;
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

    public String getStudyOfferId() {
        return studyOfferId;
    }

    public void setStudyOfferId(String studyOfferId) {
        this.studyOfferId = studyOfferId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChecklistEvent that = (ChecklistEvent) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ChecklistEvent{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", userCreated=" + userCreated +
                ", done=" + done +
                ", deadline=" + deadline +
                ", studyOfferId='" + studyOfferId + '\'' +
                '}';
    }
}
