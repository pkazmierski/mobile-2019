package com.example.kandydatpl.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.models.ChecklistEvent;

import java.text.SimpleDateFormat;

public class AddOrEditChecklistEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTitle;
    private EditText editDescription;
    private Button saveButton;
    private Button pickDateButton;
    private EditText dateDisplay;
    private Calendar sampleDate;
    private SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_checklist_event);
        editTitle = findViewById(R.id.titleEditText);
        editDescription = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);
        pickDateButton = findViewById(R.id.pickDateButton);
        dateDisplay = findViewById(R.id.dateDisplay);
        sampleDate = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy");


        Intent incomingIntent = getIntent();
        ChecklistEvent incomingItem = (ChecklistEvent) incomingIntent.getSerializableExtra("item");
        if( incomingItem.getDeadline() != null) {
            sampleDate.setTime(incomingItem.getDeadline());
        }
        dateDisplay.setText(formatter.format(sampleDate.getTime()));
        pickDateButton.setOnClickListener((v) -> {
            {
                new DatePickerDialog(this, this, sampleDate.get(Calendar.YEAR),
                        sampleDate.get(Calendar.MONTH),
                        sampleDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



        int index = (int) incomingIntent.getIntExtra("index", -1);
        editTitle.setText(incomingItem.getTitle());
        editDescription.setText(incomingItem.getDescription());

        saveButton.setOnClickListener(click -> {
            Intent listIntent = getIntent();
            ChecklistEvent listItem = new ChecklistEvent("", editTitle.getText().toString(), editDescription.getText().toString(), incomingItem.isDone(), sampleDate.getTime());
            listIntent.putExtra("newItem", listItem);
            listIntent.putExtra("index", index);
            setResult(RESULT_OK, listIntent);
            finish();
        });
    }

    private Runnable afterAddEventSuccess = () -> runOnUiThread(() ->
            Toast.makeText(this, "Item added!", Toast.LENGTH_SHORT).show());

    private Runnable afterEditEventSuccess = () -> runOnUiThread(() ->
            Toast.makeText(this, "Item changed!", Toast.LENGTH_SHORT).show());

    private Runnable afterAddEventFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, "Failed to add the item", Toast.LENGTH_SHORT).show());

    private Runnable afterEditEventFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, "Failed to edit the item", Toast.LENGTH_SHORT).show());

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        sampleDate.set(Calendar.YEAR, year);
        sampleDate.set(Calendar.MONTH, month);
        sampleDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateDisplay.setText(formatter.format(sampleDate.getTime()));
    }
}
