package com.example.kandydatpl.models;

import com.amazonaws.amplify.generated.graphql.ListCommentsQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Question {
    private String id;
    private String content;
    private int commentCount;
    private ArrayList<Comment> comments;

    public Question(@Nonnull String id, @Nonnull String content) {
        this.id = id;
        this.content = content;
        this.commentCount = 0;
        this.comments = new ArrayList<>();
    }

    public Question(@Nonnull String id, @Nonnull String content, @Nonnull int commentCount) {
        this.id = id;
        this.content = content;
        this.commentCount = commentCount;
        this.comments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (this.id.equals(""))
            this.id = id;
        else
            throw new Error("Cannot set question ID: question ID is not empty.");
    }

    public String getContent() {
        return content;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments.clear();
        this.comments.addAll(comments);
    }

    public void addComment(Comment comment) {
        comments.add(comment);
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
                ", commentCount=" + commentCount +
                '}';
    }
}
