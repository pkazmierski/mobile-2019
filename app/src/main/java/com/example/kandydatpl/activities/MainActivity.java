package com.example.kandydatpl.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amazonaws.amplify.generated.graphql.CreateCommentMutation;
import com.amazonaws.amplify.generated.graphql.CreateQuestionMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.R;
import com.example.kandydatpl.logic.Logic;
import com.example.kandydatpl.models.Comment;

import java.util.List;

import javax.annotation.Nonnull;

import type.CreateCommentInput;
import type.CreateQuestionInput;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logic.initAppSync(getApplicationContext());

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
}
