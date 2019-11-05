package com.example.kandydatpl.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amazonaws.amplify.generated.graphql.CreateCommentMutation;
import com.amazonaws.amplify.generated.graphql.CreateQuestionMutation;
import com.amazonaws.amplify.generated.graphql.UpdateQuestionMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.R;
import com.example.kandydatpl.logic.Logic;
import com.example.kandydatpl.models.Comment;

import javax.annotation.Nonnull;

import type.CreateCommentInput;
import type.CreateQuestionInput;
import type.UpdateQuestionInput;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logic.initAppSync(getApplicationContext());

//        String qid = "3152ab8a-fa1a-4c9f-8f55-cb23f524210e";
//        mutationAddComment(new Comment(null, "At XYZ.", qid));
//        mutationAddComment(new Comment(null, "At ABC.", qid));
//        mutationAddComment(new Comment(null, "At DEF.", qid));
    }

    public void goToQuestions(View view) {
        Intent questionsIntent = new Intent(this, QuestionsActivity.class);
        startActivity(questionsIntent);
    }

    public void mutationAddComment(Comment comment){
        CreateCommentInput createCommentInput = CreateCommentInput.builder()
                .content(comment.getContent())
                .commentQuestionId(comment.getQuestionId())
                .build();

        Logic.AppSync.mutate(CreateCommentMutation.builder().input(createCommentInput).build())
                .enqueue(createCommentCallback);
    }

    private GraphQLCall.Callback<CreateCommentMutation.Data> createCommentCallback = new GraphQLCall.Callback<CreateCommentMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateCommentMutation.Data> response) {
            Log.i("Results", "Added comment: " + response.data().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("Error", e.toString());
        }
    };

    public void mutationAddQuestion(String content){
        CreateQuestionInput createTodoInput = CreateQuestionInput.builder()
                .content(content)
                .build();

        Logic.AppSync.mutate(CreateQuestionMutation.builder().input(createTodoInput).build())
                .enqueue(addQuestionCallback);
    }

    private GraphQLCall.Callback<CreateQuestionMutation.Data> addQuestionCallback = new GraphQLCall.Callback<CreateQuestionMutation.Data>() {
        @Override
        public void onResponse(@Nonnull Response<CreateQuestionMutation.Data> response) {
            Log.i("Results", "Added question: " + response.data().toString());
        }

        @Override
        public void onFailure(@Nonnull ApolloException e) {
            Log.e("Error", e.toString());
        }
    };
}
