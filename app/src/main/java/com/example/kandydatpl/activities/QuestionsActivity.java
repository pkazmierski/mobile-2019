package com.example.kandydatpl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import com.amazonaws.amplify.generated.graphql.ListCommentsQuery;
import com.amazonaws.amplify.generated.graphql.ListQuestionsQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.QuestionsRecyclerViewAdapter;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.Question;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import type.ModelCommentFilterInput;
import type.ModelStringFilterInput;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;
import static com.example.kandydatpl.logic.Logic.AppSync;

public class QuestionsActivity extends AppCompatActivity {

    private static final String TAG = "QuestionsActivity";
    RecyclerView recyclerView;
    QuestionsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        runListQuestionsQuery();
        initRecyclerView();
    }

    public void addNewQuestion(View view) {
        Intent goToNewQuestionActivity = new Intent(this, AddQuestionActivity.class);
        startActivityForResult(goToNewQuestionActivity, 1);
    }

    public void setCommentsForQuestions(QuestionsRecyclerViewAdapter adapter) {
        for (Question question :
                DataStore.getQuestions()) {
            runListCommentsQuery(question, adapter);
        }
    }

    public void runListCommentsQuery(Question question, QuestionsRecyclerViewAdapter adapter) {
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

    public void runListQuestionsQuery() {
        AppSync.query(ListQuestionsQuery.builder()
                .build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(listQuestionsCallback);
    }

    private GraphQLCall.Callback<ListQuestionsQuery.Data> listQuestionsCallback = new GraphQLCall.Callback<ListQuestionsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListQuestionsQuery.Data> response) {
            assert response.data() != null;
            DataStore.setQuestions(response.data().listQuestions().items());
            setCommentsForQuestions(adapter);
            Log.i("Results", response.data().listQuestions().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        recyclerView = findViewById(R.id.questionsListRecyclerView);
        adapter = new QuestionsRecyclerViewAdapter(this, DataStore.getQuestions());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            adapter.notifyDataSetChanged();
        }
    }
}
