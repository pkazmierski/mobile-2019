package pl.mobile.kandydatpl.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import pl.mobile.kandydatpl.R;
import pl.mobile.kandydatpl.models.Question;

import java.util.Date;

import static pl.mobile.kandydatpl.logic.Logic.*;

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
        Toast.makeText(getApplicationContext(), getString(R.string.question_sent), Toast.LENGTH_SHORT).show();
        finish();
    });

    private Runnable afterQuestionCreatedFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.failed_to_send_the_question), Toast.LENGTH_LONG).show());
}
