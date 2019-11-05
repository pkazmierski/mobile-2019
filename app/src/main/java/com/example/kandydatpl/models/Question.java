package com.example.kandydatpl.models;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question {
    private String id;
    private String content;
    private ArrayList<Comment> comments;

    public Question(String id, String content) {
        this.id = id;
        this.content = content;
        this.comments = new ArrayList<>();
    }

    public Question(String id, String content, ArrayList<Comment> comments) {
        this.id = id;
        this.content = content;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public List<Comment> getComments() {
        return comments;
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
}
