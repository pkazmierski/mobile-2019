package com.example.kandydatpl.data;

import android.util.Log;

import com.amazonaws.amplify.generated.graphql.ListCommentsQuery;
import com.amazonaws.amplify.generated.graphql.ListQuestionsQuery;
import com.amazonaws.amplify.generated.graphql.ListUserEventsQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.adapters.QuestionsRecyclerViewAdapter;
import com.example.kandydatpl.logic.Logic;
import com.example.kandydatpl.models.Contact;
import com.example.kandydatpl.models.Event;
import com.example.kandydatpl.models.File;
import com.example.kandydatpl.models.Question;
import com.example.kandydatpl.models.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import type.ModelCommentFilterInput;
import type.ModelStringFilterInput;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;
import static com.example.kandydatpl.logic.Logic.appSyncClient;

public class DataStore {
    private static final String TAG = "DataStore";
    private static ArrayList<Question> questions = new ArrayList<>();
    private static UserData userData = UserData.getInstance();
    private static ArrayList<Contact> contacts = new ArrayList<>();
    private static ArrayList<File> files = new ArrayList<>();

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
}
