package com.example.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

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

import java.util.List;

import javax.annotation.Nonnull;

import type.CreateCommentInput;
import type.CreateQuestionInput;

import static com.example.kandydatpl.logic.Logic.dataProvider;

public class MainActivity extends NavigationDrawerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawer.addView(contentView, 0);

        Logic.initAppSync(getApplicationContext());

        dataProvider.getUserData(null, null);

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

    public void goToQuestions(View view) {
        Intent questionsIntent = new Intent(this, QuestionsActivity.class);
        startActivity(questionsIntent);
    }

    public void goToBookmarks(View view) {
        Intent questionsIntent = new Intent(this, QuestionsActivity.class);
        questionsIntent.putExtra("bookmarksOnly", true);
        startActivity(questionsIntent);
    }
}
