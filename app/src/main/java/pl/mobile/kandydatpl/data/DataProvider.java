package pl.mobile.kandydatpl.data;

import pl.mobile.kandydatpl.models.ChecklistEvent;
import pl.mobile.kandydatpl.models.Comment;
import pl.mobile.kandydatpl.models.Question;
import pl.mobile.kandydatpl.models.StudyOffer;

import java.util.HashMap;

import javax.annotation.Nonnull;

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
    void switchOfferStatus(Runnable onSuccess, Runnable onFailure, StudyOffer studyOffer);

    //Admin functions
    void addStudyOffer(Runnable onSuccess, Runnable onFailure, @Nonnull StudyOffer studyOffer);
    void modifyStudyOffer(Runnable onSuccess, Runnable onFailure, StudyOffer studyOffer);
    void removeStudyOffer(Runnable onSuccess, Runnable onFailure, StudyOffer studyOffer);
    void getUserDataOnLogin(Runnable onSuccess, Runnable onFailure);
    void createNewUserData(Runnable onSuccess, Runnable onFailure);

    //Events
    void getUserEvents(Runnable onSuccess, Runnable onFailure);
    void getPublicEvents(Runnable onSuccess, Runnable onFailure);
    void getAllEvents(Runnable onSuccess, Runnable onUserEventsFailure, Runnable onPublicEventsFailure);
    void updateSingleUserEvent(Runnable onSuccess, Runnable onFailure, ChecklistEvent checklistEvent);
    void createSingleUserEvent(Runnable onSuccess, Runnable onFailure, ChecklistEvent checklistEvent);
    void removeSingleUserEvent(Runnable onSuccess, Runnable onFailure, ChecklistEvent checklistEvent);
    //    void updateUserEvents(Runnable onSuccess, Runnable onUpdateSingleEventFailure, Runnable onCreateSingleEventFailure);
    void setEventsOrder(Runnable onSuccess, Runnable onFailure, HashMap<String, Integer> eventOrder);


    //Admin functions
    void addPublicEvent(Runnable onSuccess, Runnable onFailure, ChecklistEvent checkListEvent);
}
