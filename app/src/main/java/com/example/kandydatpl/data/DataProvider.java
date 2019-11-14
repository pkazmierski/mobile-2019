package com.example.kandydatpl.data;

import com.example.kandydatpl.models.Comment;
import com.example.kandydatpl.models.Event;
import com.example.kandydatpl.models.Question;

import java.util.HashMap;

public interface DataProvider {
    //Questions
    void getQuestions(Runnable onSuccess, Runnable onFailure);
    boolean moreQuestionsAvailable();
    void getNextQuestions(Runnable onSuccess, Runnable onFailure);
    void addQuestion(Runnable onSuccess, Runnable onFailure, Question question);
    void bookmarkQuestion(Runnable onSuccess, Runnable onFailure, Question question, boolean add);

    //Comments
    void getComments(Runnable onSuccess, Runnable onFailure, Question question);
    void addComment(Runnable onSuccess, Runnable onFailure, Comment comment);
    void removeComment(Runnable onSuccess, Runnable onFailure, Comment comment);
    void modifyComment(Runnable onSuccess, Runnable onFailure, Comment comment);
    void changeLikeStatusComment(Runnable onSuccess, Runnable onFailure, Comment comment, boolean add);

    //User
    void getUserData(Runnable onSuccess, Runnable onFailure);

    //Events
    void getUserEvents(Runnable onSuccess, Runnable onFailure);
    void getPublicEvents(Runnable onSuccess, Runnable onFailure);
    void getAllEvents(Runnable onSuccess, Runnable onUserEventsFailure, Runnable onPublicEventsFailure);
    void updateSingleUserEvent(Runnable onSuccess, Runnable onFailure, Event event);
    void createSingleUserEvent(Runnable onSuccess, Runnable onFailure, Event event);
//    void updateUserEvents(Runnable onSuccess, Runnable onUpdateSingleEventFailure, Runnable onCreateSingleEventFailure);
    void setEventsOrder(Runnable onSuccess, Runnable onFailure, HashMap<Event, Integer> eventOrder);
}
