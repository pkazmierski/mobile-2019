package com.example.kandydatpl.models;

import com.amazonaws.mobile.client.AWSMobileClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserData {
    private String userId;
    private String login;
    private String fullName;
    private static UserData instance;
    private HashMap<String, Integer> eventsOrder;
    private ArrayList<String> questionBookmarks;
    private ArrayList<String> activeOffersIds = new ArrayList<>();
    private ArrayList<String> donePublicEvents = new ArrayList<>();

    private UserData() {
        userId = AWSMobileClient.getInstance().getIdentityId();
        login = AWSMobileClient.getInstance().getUsername();
        fullName = login;
        eventsOrder = new HashMap<>();
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

    public HashMap<String, Integer> getEventsOrder() {
        return eventsOrder;
    }

    public void setEventsOrder(HashMap<String, Integer> eventsOrder) {
        this.eventsOrder = eventsOrder;
    }

    public List<String> getActiveOffersIds() {
        return activeOffersIds;
    }

    public ArrayList<String> getDonePublicEvents() {
        return donePublicEvents;
    }

    public void setActiveOffersIds(ArrayList<String> activeOffersIds) {
        this.activeOffersIds = activeOffersIds;
    }

    public void addActiveOfferId(String studyOfferId) {
        if(!activeOffersIds.contains(studyOfferId))
            activeOffersIds.add(studyOfferId);
    }

    public void removeActiveOfferId(String studyOfferId) {
        activeOffersIds.remove(studyOfferId);
    }

    public void setDonePublicEvents(ArrayList<String> donePublicEvents) {
        this.donePublicEvents = donePublicEvents;
    }

    public void removeDonePublicEvent(ArrayList donePublicEventId) {
        donePublicEvents.remove(donePublicEventId);
    }
}
