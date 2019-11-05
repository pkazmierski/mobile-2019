package com.example.kandydatpl.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.amazonaws.amplify.generated.graphql.ListQuestionsQuery;
import com.amazonaws.amplify.generated.graphql.ListQuestionsQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.QuestionsRecyclerViewAdapter;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.Event;

import java.sql.Date;

import javax.annotation.Nonnull;

import static com.example.kandydatpl.logic.Logic.AppSync;

public class QuestionsActivity extends AppCompatActivity {

    private static final String TAG = "QuestionsActivity";
    RecyclerView recyclerView;
    QuestionsRecyclerViewAdapter adapter;

    //vars
//    private ArrayList<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Event testEvent = new Event("testId", "testTitle", "testDesc", new Date(2019, 10, 20), true, false);
        DataStore.getEvents().add(testEvent);

        runQuery();
        initRecyclerView();
    }

    public void runQuery() {
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
            runOnUiThread(new Runnable(){
                public void run() {
                    Log.i("UI", "Updating RecyclerView");
                    adapter.notifyDataSetChanged();
                }
            });
            Log.i("Results", response.data().listQuestions().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new QuestionsRecyclerViewAdapter(this, DataStore.getQuestions());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
