package com.example.kandydatpl.models;

import java.util.Objects;

public class Comment {
    private String id;
    private String content;
    private String questionId;

//    public Comment(String id, String content) {
//        this.id = id;
//        this.content = content;
//    }

    public Comment(String id, String content, String questionId) {
        this.id = id;
        this.content = content;
        this.questionId = questionId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if(this.id.equals(""))
            this.id = id;
        else
            throw new Error("Cannot set comment ID: current ID is not empty.");
    }

    public String getContent() {
        return content;
    }

    public String getQuestionId() {
        return questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", questionId='" + questionId + '\'' +
                '}';
    }
}
