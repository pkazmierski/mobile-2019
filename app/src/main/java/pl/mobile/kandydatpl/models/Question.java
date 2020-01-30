package pl.mobile.kandydatpl.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Nonnull;

public class Question {
    private String id;
    private String content;
    private int commentCount;
    private ArrayList<Comment> comments;
    private Date createdAt;
    private String creator;

    public Question(@Nonnull String id, @Nonnull String content, @Nonnull Date createdAt) {
        this.id = id;
        this.content = content;
        this.commentCount = 0;
        this.comments = new ArrayList<>();
        this.createdAt = createdAt;
    }

    public Question(@Nonnull String id, @Nonnull String content, @Nonnull int commentCount, @Nonnull Date createdAt) {
        this.id = id;
        this.content = content;
        this.commentCount = commentCount;
        this.comments = new ArrayList<>();
        this.createdAt = createdAt;
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
        comments.add(0, comment);
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
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
