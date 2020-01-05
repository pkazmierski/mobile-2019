package com.example.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateCommentMutation;
import com.amazonaws.amplify.generated.graphql.CreateQuestionMutation;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.R;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.logic.Logic;
import com.example.kandydatpl.models.Comment;
import com.example.kandydatpl.models.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;

import type.CreateCommentInput;
import type.CreateQuestionInput;

import static com.example.kandydatpl.logic.Logic.dataProvider;

public class MainActivity extends NavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideSoftKeyBoard();

        Logic.initAppSync(getApplicationContext());

        ProgressDialog getUserDataDialog = new ProgressDialog(MainActivity.this);

        getUserDataDialog.setMessage("Getting user data...");
        getUserDataDialog.setTitle("DB query");
        getUserDataDialog.setIndeterminate(true);
        getUserDataDialog.setCancelable(false);
        getUserDataDialog.show();

        Runnable getDataSuccess = () -> runOnUiThread(() -> {
            if (DataStore.getUserData() == null) {
                dataProvider.createNewUserData(createUserDataSuccess, createUserDataFailure);
                DataStore.setUserData(UserData.getInstance());
                UserData.getInstance().setQuestionBookmarks(new ArrayList<>());
                UserData.getInstance().setEventsOrder(new HashMap<>());
            }
            getUserDataDialog.dismiss();
        });

        dataProvider.getUserDataOnLogin(getDataSuccess, getUserDataFailure);

//        setContentView(R.layout.activity_main);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);

//        String qid = "b13e9428-d1db-4f51-81a7-7aa24a2256ae";
//        mutationAddComment(new Comment(null, "At XYZ.", qid));
//        mutationAddComment(new Comment(null, "At ABC.", qid));
//        mutationAddComment(new Comment(null, "At DEF.", qid));

//        List<String> commentIds = new ArrayList<>();
//        commentIds.add("4597f234-3e3c-4cb9-a785-9a8435c97959");
//        commentIds.add("79e33384-1f37-4b2e-971f-5111bfad2cf7");
//        commentIds.add("bc44ea2f-0399-43dd-87bd-2993353824c4");
//
//        mutationAddQuestion("Where can I get some cheap and tasty food?", commentIds);
//        mutationAddQuestion("What is the minimum level of English proficiency required?");
//        mutationAddQuestion("How much does the student housing cost per month?");
//        mutationAddQuestion("When is the deadline for application?");
    }

    private Runnable getUserDataFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), "Failed to get user data", Toast.LENGTH_LONG).show());

    private Runnable createUserDataSuccess = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), "Created user data", Toast.LENGTH_LONG).show());

    private Runnable createUserDataFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), "Failed to create user data", Toast.LENGTH_LONG).show());

    public void goToQuestions(View view) {
        Intent questionsIntent = new Intent(this, QuestionsActivity.class);
        startActivity(questionsIntent);
    }

    public void goToBookmarks(View view) {
        Intent questionsIntent = new Intent(this, QuestionsActivity.class);
        questionsIntent.putExtra("bookmarksOnly", true);
        startActivity(questionsIntent);
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
