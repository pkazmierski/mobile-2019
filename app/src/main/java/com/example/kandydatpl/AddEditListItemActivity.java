package com.example.kandydatpl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddEditListItemActivity extends AppCompatActivity {

    private EditText editTitle;
    private EditText editDescription;
    private EditText editDate;
    private Button saveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_list_item);
        editTitle = findViewById(R.id.titleEditText);
        editDescription = findViewById(R.id.descriptionEditText);
        editDate = findViewById(R.id.dateEditText);
        saveButton = findViewById(R.id.saveButton);

        Intent incomingIntent = getIntent();
        ChecklistItem incomingItem = (ChecklistItem) incomingIntent.getSerializableExtra("item");
        int index = (int) incomingIntent.getIntExtra("index", -1);
        editTitle.setText(incomingItem.getTitle());
        editDescription.setText(incomingItem.getDescription());
        editDate.setText(new SimpleDateFormat("DD/MM/YYYY").format(incomingItem.getDeadline()));

        saveButton.setOnClickListener(click -> {
            Intent listIntent = getIntent();
            try {
                ChecklistItem listItem = new ChecklistItem(editTitle.getText().toString(), editDescription.getText().toString(), false ,
                        new SimpleDateFormat("DD/MM/YYYY").parse(editDate.getText().toString()));
                listIntent.putExtra("newItem", listItem);
                listIntent.putExtra("index", index);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            setResult(RESULT_OK, listIntent);
            finish();
        });
    }


}
