package com.example.kandydatpl.data;

import com.example.kandydatpl.models.Event;
import com.example.kandydatpl.models.Question;
import com.example.kandydatpl.models.UserData;

import java.util.ArrayList;

public class DataStore {
    private static final String TAG = "DataStore";
    private static ArrayList<Question> questions = new ArrayList<>();
    private static UserData userData = UserData.getInstance();
    private static ArrayList<Event> events = new ArrayList<>();

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

    public static void addEvents(ArrayList<Event> eventsToArrayList) {
        DataStore.events.addAll(eventsToArrayList);
    }

    public static void clearEvents() {
        DataStore.events.clear();
    }
}
