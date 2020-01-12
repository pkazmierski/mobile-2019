package com.example.kandydatpl.data;

import com.example.kandydatpl.models.ChecklistEvent;
import com.example.kandydatpl.models.Contact;
import com.example.kandydatpl.models.File;
import com.example.kandydatpl.models.Question;
import com.example.kandydatpl.models.StudyOffer;
import com.example.kandydatpl.models.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class DataStore {
    private static final String TAG = "DataStore";
    private static ArrayList<Question> questions = new ArrayList<>();

    private static ArrayList<ChecklistEvent> allChecklistEvents = new ArrayList<>();
    private static ArrayList<ChecklistEvent> userChecklistEvents = new ArrayList<>();
    private static ArrayList<ChecklistEvent> publicChecklistEvents = new ArrayList<>();
    private static UserData userData = UserData.getInstance();
    private static ArrayList<Contact> contacts = new ArrayList<>();
    private static ArrayList<File> files = new ArrayList<>();
    private static ArrayList<StudyOffer> studyOffers = new ArrayList<>();

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

    public static ArrayList<Contact> getContacts() {
        return contacts;
    }

    public static void setContacts(ArrayList<Contact> contacts) {
        DataStore.contacts.clear();
        DataStore.contacts.addAll(contacts);
    }

    public static ArrayList<File> getFiles() {
        return files;
    }

    public static void setFiles(ArrayList<File> files) {
        DataStore.files.clear();
        DataStore.files.addAll(files);
    }

    public static ArrayList<StudyOffer> getStudyOffers() {
        return studyOffers;
    }

    public static void setStudyOffers(ArrayList<StudyOffer> studyOffers) {
        DataStore.studyOffers.clear();
        DataStore.studyOffers.addAll(studyOffers);
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

    public static  List<StudyOffer> getOffersWithTag(String tag) {
        LinkedHashSet<StudyOffer> matchingStudyOffers = new LinkedHashSet<>();
        for (StudyOffer studyOffer : studyOffers) {
            if(studyOffer.getTags().contains(tag))
                matchingStudyOffers.add(studyOffer);
        }
        return new ArrayList<>(matchingStudyOffers);
    }

//    public static void updateEvent(ChecklistEvent checklistEvent) {
//        DataStore.checklistEvents.set(DataStore.checklistEvents.indexOf(checklistEvent), checklistEvent);
//    }
}
