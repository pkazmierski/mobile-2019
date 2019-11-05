package com.example.kandydatpl.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.amazonaws.amplify.generated.graphql.ListCommentsQuery;
import com.amazonaws.amplify.generated.graphql.ListQuestionsQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.CommentsRecyclerViewAdapter;
import com.example.kandydatpl.adapters.QuestionsRecyclerViewAdapter;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.Comment;
import com.example.kandydatpl.models.Question;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import type.ModelCommentFilterInput;
import type.ModelStringFilterInput;

import static com.example.kandydatpl.logic.Logic.AppSync;

public class QuestionDetailsActivity extends AppCompatActivity {

    private static final String TAG = "QuestionDetailsActivity";
    RecyclerView recyclerView;
    CommentsRecyclerViewAdapter adapter;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);
        Log.d(TAG, "onCreate: questionId" + getIntent().getExtras().getString("questionId"));
        question = DataStore.getQuestion(getIntent().getExtras().getString("questionId")); //todo zmienic na parcelable albo cos innego
        TextView questionDetailsContentTxt = findViewById(R.id.questionDetailsContentTxt);
        questionDetailsContentTxt.setText(question.getContent());

        runListCommentsQuery();
        initRecyclerView();
    }

    public void runListCommentsQuery() {
        ModelStringFilterInput questionIdFilter = ModelStringFilterInput.builder().eq(question.getId()).build();
        ModelCommentFilterInput commentFilter = ModelCommentFilterInput.builder().questionId(questionIdFilter).build();

        AppSync.query(ListCommentsQuery.builder()
                .build())
                .responseFetcher(AppSyncResponseFetchers.CACHE_AND_NETWORK)
                .enqueue(listCommentsCallback);
    }

    private GraphQLCall.Callback<ListCommentsQuery.Data> listCommentsCallback = new GraphQLCall.Callback<ListCommentsQuery.Data>() {
        @Override
        public void onResponse(@Nonnull Response<ListCommentsQuery.Data> response) {
            assert response.data() != null;
            assert response.data().listComments().items() != null;

            question.setComments(response.data().listComments().items());
            runOnUiThread(new Runnable(){
                public void run() {
                    Log.i("UI", "QuestionDetailsActivity: Updating RecyclerView");
                    adapter.notifyDataSetChanged();
                }
            });
            Log.i("Results", response.data().listComments().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("ERROR", e.toString());
        }
    };

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        recyclerView = findViewById(R.id.questionsDetailsRecyclerView);
        adapter = new CommentsRecyclerViewAdapter(this, question.getComments());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
