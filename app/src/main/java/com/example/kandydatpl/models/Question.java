package com.example.kandydatpl.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question {
    private String id;
    private String content;
    private ArrayList<String> commentIds;

    public Question(String id, String content) {
        this.id = id;
        this.content = content;
        this.commentIds = new ArrayList<>();
    }

    public Question(String id, String content, ArrayList<String> commentIds) {
        this.id = id;
        this.content = content;
        this.commentIds = commentIds;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public List<String> getCommentIds() {
        return commentIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return id.equals(question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", commentIds=" + commentIds +
                '}';
    }
}
