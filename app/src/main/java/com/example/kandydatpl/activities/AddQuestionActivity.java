package com.example.kandydatpl.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.Question;

import java.util.Date;

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
        if(!newQuestionContentTxt.getText().toString().equals("")) {
            Question question = new Question("", newQuestionContentTxt.getText().toString(), new Date());
            dataProvider.addQuestion(afterQuestionCreatedSuccess, afterQuestionCreatedFailure, question);
        }
    }

    private Runnable afterQuestionCreatedSuccess = () -> runOnUiThread(() -> {
        Toast.makeText(getApplicationContext(), "Question sent", Toast.LENGTH_SHORT).show();
        finish();
    });

    private Runnable afterQuestionCreatedFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), "Failed to send the question", Toast.LENGTH_LONG).show());
}
