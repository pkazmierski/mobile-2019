package com.example.kandydatpl.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.ContactsRecyclerViewAdapter;
import com.example.kandydatpl.models.ChecklistEvent;

import java.text.SimpleDateFormat;

import static com.example.kandydatpl.activities.ChecklistEventActivity.editItemRequest;
import static com.example.kandydatpl.activities.ChecklistEventActivity.newItemRequest;
import static com.example.kandydatpl.logic.Logic.dataProvider;

public class AddOrEditChecklistEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTitle;
    private EditText editDescription;
    private Button saveButton;
    private Button pickDateButton;
    private Button addContactButton;
    private Button saveContactButton;
    private Dialog contactDialog;
    private EditText contactDisplay;
    private EditText dateDisplay;
    private Calendar sampleDate;
    private SimpleDateFormat formatter;
    private static final String TAG = "AddOrEditChecklistEventActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_checklist_event);
        editTitle = findViewById(R.id.titleEditText);
        editDescription = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);
        pickDateButton = findViewById(R.id.pickDateButton);
        addContactButton = findViewById(R.id.addcontactButton);
        saveContactButton = findViewById(R.id.saveContactButton);
        dateDisplay = findViewById(R.id.dateDisplay);
        contactDisplay = findViewById(R.id.contactDisplay);
        sampleDate = Calendar.getInstance();
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        contactDialog = new Dialog(this);


        Intent incomingIntent = getIntent();
        ChecklistEvent incomingItem = (ChecklistEvent) incomingIntent.getSerializableExtra("item");
        Log.e(TAG, "incomingItem: " + incomingItem.toString());

        if (incomingItem.getDeadline() != null) {
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

        int requestCode = (int) incomingIntent.getIntExtra("requestCode", -1);
        int index = (int) incomingIntent.getIntExtra("index", -1);
        editTitle.setText(incomingItem.getTitle());
        editDescription.setText(incomingItem.getDescription());

        saveButton.setOnClickListener(click -> {
            if(!hasNetworkConnection()){
                Toast.makeText(this, "Cannot save when no connection", Toast.LENGTH_SHORT).show();
            } else {
                Intent listIntent = getIntent();
                ChecklistEvent listItem = new ChecklistEvent(requestCode == newItemRequest ? "" : incomingItem.getId(), editTitle.getText().toString(), editDescription.getText().toString(), incomingItem.isDone(), sampleDate.getTime());
                listIntent.putExtra("newItem", listItem);
                listIntent.putExtra("index", index);
                setResult(RESULT_OK, listIntent);
                finish();
            }
        });

        addContactButton.setOnClickListener((v) -> {
            Button  pickContactButton;
            TextView contactText;
            contactDialog.setContentView(R.layout.activity_add_contact);
            contactDialog.setCanceledOnTouchOutside(true);
            pickContactButton = (Button) contactDialog.findViewById(R.id.pickContactButton);
            contactText = (TextView) contactDialog.findViewById(R.id.contactText);
            pickContactButton.setOnClickListener((w) -> {
                contactDisplay.setText(contactText.getText());
            });
            contactDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            contactDialog.show();
        });

    }

    private boolean hasNetworkConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        Network[] networks = cm.getAllNetworks();
        if (cm != null) {
            for (Network netinfo : networks) {
                NetworkInfo ni = cm.getNetworkInfo(netinfo);
                if (ni.isConnected() && ni.isAvailable()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        sampleDate.set(Calendar.YEAR, year);
        sampleDate.set(Calendar.MONTH, month);
        sampleDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateDisplay.setText(formatter.format(sampleDate.getTime()));
    }

}
