package com.example.kandydatpl.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Comment {
    private String id;
    private String content;
    private String questionId;
    private Date createdAt;
    private ArrayList<String> likedBy;

//    public Comment(String id, String content) {
//        this.id = id;
//        this.content = content;
//    }

    public Comment(String id, String content, String questionId, Date createdAt) {
        this.id = id;
        this.content = content;
        this.questionId = questionId;
        this.createdAt = createdAt;
        this.likedBy = new ArrayList<>();
    }

    public Comment(String id, String content, String questionId, Date createdAt, ArrayList<String> likedBy) {
        this.id = id;
        this.content = content;
        this.questionId = questionId;
        this.createdAt = createdAt;
        if(likedBy != null) {
            this.likedBy = likedBy;
        } else {
            this.likedBy = new ArrayList<>();
        }
    }

    public String getId() {
        return id;
    }

    public ArrayList<String> getLikedBy() {
        return likedBy;
    }

    public void setId(String id) {
        if (this.id.equals(""))
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

    public boolean isLikedBy(String userId) {
        return likedBy.contains(userId);
    }

    public void changeLikeStatus(String userId) {
        if (isLikedBy(userId))
            likedBy.remove(userId);
        else
            likedBy.add(userId);
    }

    public int getLikeCount() {
        return likedBy.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id.equals(comment.id);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
