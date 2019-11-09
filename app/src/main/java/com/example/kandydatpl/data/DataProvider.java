package com.example.kandydatpl.data;

import com.example.kandydatpl.models.Comment;
import com.example.kandydatpl.models.Question;

import java.util.ArrayList;
import java.util.List;

public interface DataProvider {
    //Questions
    void getQuestions(Runnable runnable);
    boolean moreQuestionsAvailable();
    void getNextQuestions(Runnable runnable);
    void addQuestion(Runnable runnable, Question question);
    void bookmarkQuestion(Runnable runnable, Question question);

    //Comments
    void getComments(Runnable runnable, Question question);
    void addComment(Runnable runnable, Comment comment);
    void removeComment(Runnable runnable, Comment comment);
    void modifyComment(Runnable runnable, Comment comment);
    void changeLikeStatusComment(Runnable runnable, Comment comment);
}
