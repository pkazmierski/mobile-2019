package com.example.kandydatpl.data;

import com.example.kandydatpl.models.Comment;
import com.example.kandydatpl.models.Question;
import com.example.kandydatpl.models.StudyOffer;

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

    //Contacts
    void getContacts(Runnable onSuccess, Runnable onFailure);

    //Files
    void getFiles(Runnable onSuccess, Runnable onFailure);

    //Study offers
    void getStudyOffers(Runnable onSuccess, Runnable onFailure);

    //Admin functions
    void addStudyOffer(Runnable onSuccess, Runnable onFailure, StudyOffer studyOffer);
    void modifyStudyOffer(Runnable onSuccess, Runnable onFailure, StudyOffer studyOffer);
    void removeStudyOffer(Runnable onSuccess, Runnable onFailure, StudyOffer studyOffer);
}
