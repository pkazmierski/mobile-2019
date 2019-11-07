package com.example.kandydatpl.data;

import android.util.Log;

import com.amazonaws.amplify.generated.graphql.ListCommentsQuery;
import com.amazonaws.amplify.generated.graphql.ListQuestionsQuery;
import com.amazonaws.amplify.generated.graphql.ListUserEventsQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.api.ResponseWriter;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.adapters.CommentsRecyclerViewAdapter;
import com.example.kandydatpl.adapters.QuestionsRecyclerViewAdapter;
import com.example.kandydatpl.logic.Logic;
import com.example.kandydatpl.models.Comment;
import com.example.kandydatpl.models.Event;
import com.example.kandydatpl.models.Question;

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
import static com.example.kandydatpl.logic.Logic.AppSync;

public class DataStore {
    private static final String TAG = "DataStore";
    private static ArrayList<Event> events = new ArrayList<>();
    private static ArrayList<Question> questions = new ArrayList<>();

    public static ArrayList<Event> getEvents() {
        return events;
    }
    public static ArrayList<Question> getQuestions() { return questions; }

//    public static void setEvents(ArrayList<Event> events) {
//        DataStore.events = events;
//    }

    public static Question getQuestion(String questionId) {
        for (Question q : questions) {
            if(q.getId().equals(questionId))
                return q;
        }
        return null;
    }

    public static void setEvents(List<ListUserEventsQuery.Item> items) {
        events.clear();
        for (ListUserEventsQuery.Item item : items) {
            try {
                JSONObject jsonEvent = new JSONObject(item.data());
                Event event = new Event(
                        item.id(),
                        jsonEvent.getString("title"),
                        jsonEvent.getString("description"),
                        Logic.defaultFormat.parse(jsonEvent.getString("date")),
                        true,
                        jsonEvent.getBoolean("completed")
                        );
//                if(!events.contains(event)) {
                    events.add(event);
                    Log.d(TAG, "setEvents: adding new user event: " + event.toString());
//                }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }

        }
    }

    public static void setQuestions(List<ListQuestionsQuery.Item> items) {
        questions.clear();

        for (ListQuestionsQuery.Item item : items) {

            ArrayList<String> commentIds;
            if(item.commentIds() != null)
                commentIds = new ArrayList<>(item.commentIds());
            else
                commentIds = new ArrayList<>();
            Question question = new Question(item.id(), item.content(), commentIds);
            questions.add(question);

            Log.d(TAG, "setQuestions: adding new user question: " + question.toString());
        }
    }

    public static void setCommentsForQuestions(QuestionsRecyclerViewAdapter adapter) {
        for (Question question :
                questions) {
            runListCommentsQuery(question, adapter);
        }
    }

    static public void runListCommentsQuery(Question question, QuestionsRecyclerViewAdapter adapter) {
        ModelStringFilterInput questionIdFilter = ModelStringFilterInput.builder().eq(question.getId()).build();
        ModelCommentFilterInput commentFilter = ModelCommentFilterInput.builder().questionId(questionIdFilter).build();

        GraphQLCall.Callback<ListCommentsQuery.Data> listCommentsCallback = new GraphQLCall.Callback<ListCommentsQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<ListCommentsQuery.Data> response) {
                assert response.data() != null;
                assert response.data().listComments().items() != null;
                ArrayList<String> commentIds = new ArrayList<>();
                for (ListCommentsQuery.Item item : response.data().listComments().items()) {
                    commentIds.add(item.id());
                }
                question.setCommentIds(commentIds);
                runOnUiThread(new Runnable(){
                    public void run() {
                        Log.i("UI", "QuestionsActivity: Updating RecyclerView");
                        adapter.notifyDataSetChanged();
                    }
                });
                Log.d("Results", response.data().listComments().toString());
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e("ERROR", e.toString());
            }
        };

        AppSync.query(ListCommentsQuery.builder()
                .filter(commentFilter)
                .build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(listCommentsCallback);
    }
}
