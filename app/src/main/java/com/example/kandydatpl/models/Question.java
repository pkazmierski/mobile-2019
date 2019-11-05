package com.example.kandydatpl.models;

import com.amazonaws.amplify.generated.graphql.ListCommentsQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question {
    private String id;
    private String content;
    private ArrayList<String> commentIds;
    private ArrayList<Comment> comments;

    public Question(String id, String content) {
        this.id = id;
        this.content = content;
        this.commentIds = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Question(String id, String content, ArrayList<String> commentIds) {
        this.id = id;
        this.content = content;
        this.commentIds = commentIds;
        this.comments = new ArrayList<>();
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

    public void setCommentIds(ArrayList<String> commentIds) {
        this.commentIds = commentIds;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public void setComments(List<ListCommentsQuery.Item> dbComments) {
        comments.clear();

        for (ListCommentsQuery.Item item : dbComments) {
            comments.add(new Comment(item.id(), item.content(), item.questionId()));
        }
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
