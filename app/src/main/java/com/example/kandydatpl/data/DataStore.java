package com.example.kandydatpl.data;

import com.amazonaws.amplify.generated.graphql.CreateUserEventMutation;
import com.example.kandydatpl.models.ChecklistEvent;
import com.example.kandydatpl.models.Question;
import com.example.kandydatpl.models.UserData;

import java.util.ArrayList;
import java.util.Iterator;

public class DataStore {
    private static final String TAG = "DataStore";
    private static ArrayList<Question> questions = new ArrayList<>();
    private static UserData userData;

    private static ArrayList<ChecklistEvent> allChecklistEvents = new ArrayList<>();
    private static ArrayList<ChecklistEvent> userChecklistEvents = new ArrayList<>();
    private static ArrayList<ChecklistEvent> publicChecklistEvents = new ArrayList<>();

    public static ArrayList<Question> getQuestions() {
        return questions;
    }

    public static Question getQuestion(String questionId) {
        for (Question q : questions) {
            if(q.getId().equals(questionId))
                return q;
        }
        return null;
    }

    public static ArrayList<ChecklistEvent> getAllChecklistEvents() {
        return allChecklistEvents;
    }

    public static void setQuestions(ArrayList<Question> questions) {
        DataStore.questions.clear();
        DataStore.questions.addAll(questions);
    }

    public static void addQuestion(Question question) {
        questions.add(0, question);
    }

    public static void removeQuestion(Question question) {
        questions.remove(question);
    }

    public static UserData getUserData() {
        return userData;
    }

    public static void addEvents(ArrayList<ChecklistEvent> eventsToArrayList) {
        DataStore.allChecklistEvents.removeAll(eventsToArrayList);
        DataStore.allChecklistEvents.addAll(eventsToArrayList);
    }

    public static void clearEvents() {
        DataStore.allChecklistEvents.clear();
    }

    public static void addEvent(ChecklistEvent checklistEvent) {
        DataStore.allChecklistEvents.add(checklistEvent);
    }

    public static void setUserData(UserData userData) {
        DataStore.userData = userData;
    }


    public static ArrayList<ChecklistEvent> getUserChecklistEvents() {
        return userChecklistEvents;
    }

    public static void setUserChecklistEvents(ArrayList<ChecklistEvent> userChecklistEvents) {
        DataStore.userChecklistEvents = userChecklistEvents;
        Iterator<ChecklistEvent> iter = allChecklistEvents.iterator();
        while (iter.hasNext()) {
            ChecklistEvent e = iter.next();

            if (e.isUserCreated()) {
                iter.remove();
            }
        }
        allChecklistEvents.addAll(userChecklistEvents);
    }

    public static ArrayList<ChecklistEvent> getPublicChecklistEvents() {
        return publicChecklistEvents;
    }

    public static void setPublicChecklistEvents(ArrayList<ChecklistEvent> publicChecklistEvents) {
        DataStore.publicChecklistEvents = publicChecklistEvents;
        Iterator<ChecklistEvent> iter = allChecklistEvents.iterator();
        while (iter.hasNext()) {
            ChecklistEvent e = iter.next();
            if (!e.isUserCreated()) {
                iter.remove();
            }
        }
        allChecklistEvents.addAll(publicChecklistEvents);
    }

//    public static void updateEvent(ChecklistEvent checklistEvent) {
//        DataStore.checklistEvents.set(DataStore.checklistEvents.indexOf(checklistEvent), checklistEvent);
//    }
}
