package com.example.kandydatpl.models;

import com.amazonaws.mobile.client.AWSMobileClient;

import java.util.ArrayList;
import java.util.HashMap;

public class UserData {
    private String userId;
    private String login;
    private String fullName;
    private static UserData instance;
    private HashMap<Event, Integer> eventsOrder;
    private ArrayList<String> questionBookmarks;

    private UserData() {
        userId = AWSMobileClient.getInstance().getIdentityId();
        login = AWSMobileClient.getInstance().getUsername();
        fullName = login;
    }

    public static UserData getInstance() {
        if (instance == null)
            instance = new UserData();
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<String> getQuestionBookmarks() {
        return questionBookmarks;
    }

    public void setQuestionBookmarks(ArrayList<String> questionBookmarks) {
        if(questionBookmarks == null)
            this.questionBookmarks = new ArrayList<>();
        else
            this.questionBookmarks = questionBookmarks;
    }

    public void addQuestionBookmark(Question question) {
        this.questionBookmarks.add(question.getId());
    }

    public void addQuestionBookmark(String questionId) {
        this.questionBookmarks.add(questionId);
    }

    public void removeQuestionBookmark(Question question) {
        this.questionBookmarks.remove(question.getId());
    }

    public void removeQuestionBookmark(String questionId) {
        this.questionBookmarks.remove(questionId);
    }

    public String getLogin() {
        return login;
    }

    public boolean isQuestionBookmarked(Question question) {
        return questionBookmarks.contains(question.getId());
    }

    public String getFullName() {
        return fullName;
    }

    public HashMap<Event, Integer> getEventsOrder() {
        return eventsOrder;
    }

    public void setEventsOrder(HashMap<Event, Integer> eventsOrder) {
        this.eventsOrder = eventsOrder;
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userId='" + userId + '\'' +
                ", login='" + login + '\'' +
                ", fullName='" + fullName + '\'' +
                ", eventsOrder=" + eventsOrder +
                ", questionBookmarks=" + questionBookmarks +
                '}';
    }
}
