package com.example.kandydatpl.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.amazonaws.amplify.generated.graphql.ListCommentsQuery;
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

import static com.example.kandydatpl.logic.Logic.appSyncClient;
import static com.example.kandydatpl.logic.Logic.dataProvider;

public class QuestionsActivity extends AppCompatActivity {

    private static final String TAG = "QuestionsActivity";
    RecyclerView recyclerView;
    QuestionsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        initRecyclerView();
        dataProvider.getQuestions(afterQuestionsAcquired);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private Runnable afterQuestionsAcquired = new Runnable(){
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    public void addNewQuestion(View view) {
        Intent goToNewQuestionActivity = new Intent(this, AddQuestionActivity.class);
        startActivityForResult(goToNewQuestionActivity, 1);
    }


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
