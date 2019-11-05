package com.example.kandydatpl.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amazonaws.amplify.generated.graphql.CreateQuestionMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.R;
import com.example.kandydatpl.logic.Logic;

import javax.annotation.Nonnull;

import type.CreateQuestionInput;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logic.initAppSync(getApplicationContext());
    }

    public void goToQuestions(View view) {
        Intent questionsIntent = new Intent(this, QuestionsActivity.class);
        startActivity(questionsIntent);
    }

    public void mutation(String content){
        CreateQuestionInput createTodoInput = CreateQuestionInput.builder()
                .content(content)
                .build();

        Logic.AppSync.mutate(CreateQuestionMutation.builder().input(createTodoInput).build())
                .enqueue(mutationCallback);
    }

    private GraphQLCall.Callback<CreateQuestionMutation.Data> mutationCallback = new GraphQLCall.Callback<CreateQuestionMutation.Data>() {
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
