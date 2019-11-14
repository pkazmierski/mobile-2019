package com.example.kandydatpl;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEditListItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

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
        setContentView(R.layout.activity_add_edit_list_item);
        editTitle = findViewById(R.id.titleEditText);
        editDescription = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);
        pickDateButton = findViewById(R.id.pickDateButton);
        dateDisplay = findViewById(R.id.dateDisplay);
        sampleDate = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy");


        Intent incomingIntent = getIntent();
        ChecklistItem incomingItem = (ChecklistItem) incomingIntent.getSerializableExtra("item");
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
            ChecklistItem listItem = new ChecklistItem(editTitle.getText().toString(), editDescription.getText().toString(), incomingItem.isDone(), sampleDate.getTime());
            listIntent.putExtra("newItem", listItem);
            listIntent.putExtra("index", index);
            setResult(RESULT_OK, listIntent);
            finish();
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        sampleDate.set(Calendar.YEAR, year);
        sampleDate.set(Calendar.MONTH, month);
        sampleDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateDisplay.setText(formatter.format(sampleDate.getTime()));
    }
}
