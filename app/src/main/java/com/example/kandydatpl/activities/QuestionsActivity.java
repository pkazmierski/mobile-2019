package com.example.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.QuestionsRecyclerViewAdapter;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.Question;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nullable;

import static com.example.kandydatpl.logic.Logic.dataProvider;

public class QuestionsActivity extends NavigationDrawerActivity {

    private static final String TAG = "QuestionsActivity";
    EditText searchQuestionsTxt;
    RecyclerView recyclerView;
    QuestionsRecyclerViewAdapter adapter;
    ArrayList<Question> questions = DataStore.getQuestions();
    FloatingActionButton addNewQuestionBtn;
    private boolean bookmarksOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_questions);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_questions, null, false);
        drawer.addView(contentView, 0);

        searchQuestionsTxt = findViewById(R.id.searchQuestionsTxt);
        searchQuestionsTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        addNewQuestionBtn = findViewById(R.id.addNewQuestionBtn);


        bookmarksOnly = getIntent().getBooleanExtra("bookmarksOnly", false);
        if(bookmarksOnly) {
            addNewQuestionBtn.hide();
            String questionBookmarksTitle = getResources().getString(R.string.question_bookmarks);
            actionBar.setTitle(questionBookmarksTitle);
        }
        initRecyclerView();
        dataProvider.getQuestions(afterQuestionsAcquiredSuccess, afterQuestionsAcquiredFailure);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    private void filter(String text) {
        ArrayList<Question> filteredQuestions = new ArrayList<Question>();

        for (Question question : questions) {
            if(question.getContent().toLowerCase().contains(text.toLowerCase()))
                filteredQuestions.add(question);
        }

        adapter.filterList(filteredQuestions);
    }

    private void filterBookmarksOnly() {
        questions.clear();
        for (Question question : DataStore.getQuestions()) {
            if (DataStore.getUserData().isQuestionBookmarked(question))
                questions.add(question);
        }
    }

    private Runnable afterQuestionsAcquiredSuccess = () -> runOnUiThread(() -> {
        if(bookmarksOnly) {
            filterBookmarksOnly();
        }
        adapter.notifyDataSetChanged();
    });

    private Runnable afterQuestionsAcquiredFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.get_questions_failed), Toast.LENGTH_LONG).show());

    public void addNewQuestion(View view) {
        Intent goToNewQuestionActivity = new Intent(this, AddQuestionActivity.class);
        startActivityForResult(goToNewQuestionActivity, 1);
    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview, bookmarksOnly: " + bookmarksOnly);
        recyclerView = findViewById(R.id.questionsListRecyclerView);

        if(bookmarksOnly) {
            questions = new ArrayList<>();
        }
        adapter = new QuestionsRecyclerViewAdapter(this, questions);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        filter("");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            adapter.notifyItemInserted(0);
            recyclerView.smoothScrollToPosition(0);
        }
    }

    public boolean isBookmarksOnly() {
        return bookmarksOnly;
    }

    public void updateUI(@Nullable HashMap<String, String> params) {
        assert params != null;
        questions.remove(DataStore.getQuestion(params.get("questionId")));
        adapter.notifyDataSetChanged();
    }

//    @Override
//    public void onBackPressed() {
//        this.finishAffinity();
//
//        super.onBackPressed();
//    }
}
