package com.example.kandydatpl.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.amplify.generated.graphql.CreateQuestionMutation;
import com.apollographql.apollo.GraphQLCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.example.kandydatpl.R;
import com.example.kandydatpl.logic.Logic;
import com.example.kandydatpl.models.Question;

import java.util.List;

import javax.annotation.Nonnull;

import type.CreateQuestionInput;

import static com.example.kandydatpl.logic.Logic.*;

public class AddQuestionActivity extends AppCompatActivity {

    EditText newQuestionContentTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        newQuestionContentTxt = findViewById(R.id.newQuestionContentTxt);
    }

    public void addNewQuestion(View view) {
        Question question = new Question("", newQuestionContentTxt.getText().toString());
        dataProvider.addQuestion(afterQuestionCreated ,question);
    }

    private Runnable afterQuestionCreated = new Runnable(){
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Question sent", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    };
}
