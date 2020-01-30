package pl.mobile.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import pl.mobile.kandydatpl.R;
import pl.mobile.kandydatpl.adapters.CommentsRecyclerViewAdapter;
import pl.mobile.kandydatpl.data.DataStore;
import pl.mobile.kandydatpl.models.Comment;
import pl.mobile.kandydatpl.models.Question;

import java.util.Date;

import static pl.mobile.kandydatpl.logic.Logic.dataProvider;

public class QuestionDetailsActivity extends NavigationDrawerActivity {

    private static final String TAG = "QuestionDetailsActivity";
    RecyclerView recyclerView;
    CommentsRecyclerViewAdapter adapter;
    EditText sendCommentContentTxt;
    private Question question;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_question_details);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_question_details, null, false);
        drawer.addView(contentView, 0);

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
        Toast.makeText(getApplicationContext(), getString(R.string.comment_sent), Toast.LENGTH_SHORT).show();
        adapter.notifyItemInserted(0);
        recyclerView.smoothScrollToPosition(0);
    });

    private Runnable afterCommentSentFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.failed_to_send_the_comment), Toast.LENGTH_LONG).show());

    private Runnable afterCommentListAcquiredSuccess = () -> runOnUiThread(() ->
            adapter.notifyDataSetChanged());

    private Runnable afterCommentListAcquiredFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.get_comment_list_failed), Toast.LENGTH_LONG).show());

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");
        recyclerView = findViewById(R.id.questionsDetailsRecyclerView);
        adapter = new CommentsRecyclerViewAdapter(this, question.getComments());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
