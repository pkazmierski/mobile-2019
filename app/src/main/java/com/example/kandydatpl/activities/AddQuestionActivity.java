package com.example.kandydatpl.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amazonaws.amplify.generated.graphql.CreateQuestionMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.R;
import com.example.kandydatpl.logic.Logic;

import java.util.List;

import javax.annotation.Nonnull;

import type.CreateQuestionInput;

public class AddQuestionActivity extends AppCompatActivity {

    EditText newQuestionContentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        newQuestionContentTxt = findViewById(R.id.newQuestionContentTxt);
    }

    public void addNewQuestion(View view) {
        mutationAddQuestion(newQuestionContentTxt.getText().toString(), null);
        finish();
    }

    public void mutationAddQuestion(String content, List<String> commentIds){
        CreateQuestionInput createQuestionInput = CreateQuestionInput.builder()
                .content(content)
                .commentIds(commentIds)
                .build();

        Logic.AppSync.mutate(CreateQuestionMutation.builder().input(createQuestionInput).build())
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
