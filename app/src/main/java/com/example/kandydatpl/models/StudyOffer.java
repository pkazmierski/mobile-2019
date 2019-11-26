package com.example.kandydatpl.models;

import java.util.ArrayList;
import java.util.Objects;

public class StudyOffer {
    private String id;
    private ArrayList<String> tags;
    private String content;

    public StudyOffer(String id, ArrayList<String> tags, String content) {
        this.id = id;
        this.tags = tags;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyOffer that = (StudyOffer) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StudyOffer{" +
                "id='" + id + '\'' +
                ", tags=" + tags +
                ", content='" + content + '\'' +
                '}';
    }

    //TODO remove later
    public StudyOffer() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
