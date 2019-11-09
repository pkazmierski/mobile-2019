package com.example.kandydatpl.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateCommentMutation;
import com.amazonaws.amplify.generated.graphql.ListCommentsQuery;
import com.amazonaws.mobileconnectors.appsync.fetcher.AppSyncResponseFetchers;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.CommentsRecyclerViewAdapter;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.logic.Logic;
import com.example.kandydatpl.models.Comment;
import com.example.kandydatpl.models.Question;

import javax.annotation.Nonnull;

import type.CreateCommentInput;
import type.ModelCommentFilterInput;
import type.ModelStringFilterInput;

import static com.example.kandydatpl.logic.Logic.appSyncClient;
import static com.example.kandydatpl.logic.Logic.dataProvider;

public class QuestionDetailsActivity extends AppCompatActivity {

    private static final String TAG = "QuestionDetailsActivity";
    RecyclerView recyclerView;
    CommentsRecyclerViewAdapter adapter;
    EditText sendCommentContentTxt;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_details);
        Log.d(TAG, "onCreate: questionId" + getIntent().getExtras().getString("questionId"));

        question = DataStore.getQuestion(getIntent().getExtras().getString("questionId")); //todo zmienic na parcelable albo cos innego
        TextView questionDetailsContentTxt = findViewById(R.id.questionDetailsContentTxt);
        questionDetailsContentTxt.setText(question.getContent());

        sendCommentContentTxt = findViewById(R.id.sendCommentContentTxt);

        dataProvider.getComments(afterCommentListAcquired, question);
        initRecyclerView();
    }

    public void SendNewComment(View view) {
        Comment comment = new Comment("", sendCommentContentTxt.getText().toString(), question.getId());
        dataProvider.addComment(afterCommentSent, comment);
    }

    private Runnable afterCommentSent = new Runnable(){
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    sendCommentContentTxt.setText("");
                    Toast.makeText(getApplicationContext(), "Comment sent", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    };

    private Runnable afterCommentListAcquired = new Runnable(){
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("UI", "QuestionDetailsActivity: Updating RecyclerView");
                    adapter.notifyDataSetChanged();
                }
            });
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
