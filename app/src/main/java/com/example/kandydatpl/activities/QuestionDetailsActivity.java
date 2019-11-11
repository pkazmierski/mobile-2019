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

import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.CommentsRecyclerViewAdapter;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.Comment;
import com.example.kandydatpl.models.Question;

import java.util.Date;

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

        question = DataStore.getQuestion(getIntent().getExtras().getString("questionId"));
        TextView questionDetailsContentTxt = findViewById(R.id.questionDetailsContentTxt);
        questionDetailsContentTxt.setText(question.getContent());

        sendCommentContentTxt = findViewById(R.id.sendCommentContentTxt);

        dataProvider.getComments(afterCommentListAcquiredSuccess, afterCommentListAcquiredFailure, question);
        initRecyclerView();
    }

    public void SendNewComment(View view) {
        if(!sendCommentContentTxt.getText().toString().equals("")) {
            Comment comment = new Comment("", sendCommentContentTxt.getText().toString(), question.getId(), new Date(), DataStore.getUserData().getFullName());
            dataProvider.addComment(afterCommentSentSuccess, afterCommentSentFailure, comment);
        }
    }

    private Runnable afterCommentSentSuccess = () -> runOnUiThread(() -> {
        sendCommentContentTxt.setText("");
        Toast.makeText(getApplicationContext(), "Comment sent", Toast.LENGTH_SHORT).show();
        adapter.notifyItemInserted(0);
        recyclerView.smoothScrollToPosition(0);
    });

    private Runnable afterCommentSentFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), "Failed to send the comment", Toast.LENGTH_LONG).show());

    private Runnable afterCommentListAcquiredSuccess = () -> runOnUiThread(() ->
            adapter.notifyDataSetChanged());

    private Runnable afterCommentListAcquiredFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), "Get comment list failed", Toast.LENGTH_LONG).show());

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        recyclerView = findViewById(R.id.questionsDetailsRecyclerView);
        adapter = new CommentsRecyclerViewAdapter(this, question.getComments());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
