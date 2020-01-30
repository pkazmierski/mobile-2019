package pl.mobile.kandydatpl.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class ChecklistEvent implements Serializable {

    private String id;
    private String title;
    private String description;
    private boolean userCreated;
    private boolean done;
    private Date deadline;
    private List<String> contactIds;
    private List<String> fileIds;
    private String offerId;

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

    public ChecklistEvent(String id, String title, String description, boolean userCreated, boolean done, Date deadline, String offerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.userCreated = userCreated;
        this.done = done;
        this.deadline = deadline;
        this.offerId = offerId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public List<String> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<String> contactIds) {
        this.contactIds = contactIds;
    }

    public List<String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<String> fileIds) {
        this.fileIds = fileIds;
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
                ", contactIds=" + contactIds +
                ", fileIds=" + fileIds +
                '}';
    }
}
