package pl.mobile.kandydatpl.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import pl.mobile.kandydatpl.R;
import pl.mobile.kandydatpl.adapters.ContactsRecyclerViewAdapter;
import pl.mobile.kandydatpl.adapters.FilesRecyclerViewAdapter;
import pl.mobile.kandydatpl.data.DataStore;
import pl.mobile.kandydatpl.models.ChecklistEvent;
import pl.mobile.kandydatpl.models.Contact;
import pl.mobile.kandydatpl.models.File;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static pl.mobile.kandydatpl.activities.ChecklistEventActivity.newItemRequest;
import static pl.mobile.kandydatpl.logic.Logic.dataProvider;

public class AddOrEditChecklistEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTitle;
    private EditText editDescription;
    private Button saveButton;
    private Button pickDateButton;
    private EditText dateDisplay;
    private Calendar sampleDate;
    private SimpleDateFormat formatter;

    private ChecklistEvent incomingItem;

    private RecyclerView checklistEventContactsRV;
    private RecyclerView checklistEventFilesRV;

    private FilesRecyclerViewAdapter filesRecyclerViewAdapter;
    private ContactsRecyclerViewAdapter contactsRecyclerViewAdapter;

    private static final String TAG = "AddOrEditChecklistEventActivity";

    ArrayList<File> files = new ArrayList<>();
    ArrayList<Contact> contacts = new ArrayList<>();

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
        incomingItem = (ChecklistEvent) incomingIntent.getSerializableExtra("item");
        Log.d(TAG, "incomingItem: " + incomingItem.toString());

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
            if (!hasNetworkConnection()) {
                Toast.makeText(this, getString(R.string.cannot_save_without_the_internet_connection), Toast.LENGTH_SHORT).show();
            } else {
                Intent listIntent = getIntent();
                ChecklistEvent listItem = new ChecklistEvent(requestCode == newItemRequest ? "" : incomingItem.getId(), editTitle.getText().toString(), editDescription.getText().toString(), incomingItem.isDone(), sampleDate.getTime());
                listIntent.putExtra("newItem", listItem);
                listIntent.putExtra("index", index);
                setResult(RESULT_OK, listIntent);
                finish();
            }
        });

        initContactsRV();
        initFilesRV();

        if (incomingItem.getContactIds() != null) {
            dataProvider.getContacts(getContactsSuccess, getContactsFailure);
        } else {
            Log.d(TAG, "onCreate: no contacts assigned");
        }

        if (incomingItem.getFileIds() != null) {
            dataProvider.getFiles(getFilesSuccess, getFilesFailure);
        } else {
            Log.d(TAG, "onCreate: no files assigned");
        }
    }

    private void filterFiles() {
        ArrayList<File> allFiles = DataStore.getFiles();
        files.clear();
        for (File file : allFiles) {
            if (incomingItem.getFileIds().contains(file.getId()))
                files.add(file);
        }
    }

    private void filterContacts() {
        ArrayList<Contact> allContacts = DataStore.getContacts();
        contacts.clear();
        for (Contact contact : allContacts) {
            if (incomingItem.getContactIds().contains(contact.getId()))
                contacts.add(contact);
        }
    }

    private Runnable getFilesSuccess = () -> runOnUiThread(() -> {
        filterFiles();
        filesRecyclerViewAdapter.notifyDataSetChanged();
    });

    private Runnable getFilesFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.get_files_failed), Toast.LENGTH_LONG).show());

    private Runnable getContactsSuccess = () -> runOnUiThread(() -> {
        filterContacts();
        contactsRecyclerViewAdapter.notifyDataSetChanged();
    });

    private Runnable getContactsFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.get_contacts_failed), Toast.LENGTH_LONG).show());

    private void initFilesRV() {
        checklistEventFilesRV = findViewById(R.id.checklistEventFilesRV);
        checklistEventFilesRV.setNestedScrollingEnabled(false);
        filesRecyclerViewAdapter = new FilesRecyclerViewAdapter(this, files);

        checklistEventFilesRV.setAdapter(filesRecyclerViewAdapter);
        checklistEventFilesRV.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initContactsRV() {
        checklistEventContactsRV = findViewById(R.id.checklistEventContactsRV);
        checklistEventContactsRV.setNestedScrollingEnabled(false);
        contactsRecyclerViewAdapter = new ContactsRecyclerViewAdapter(this, contacts);

        checklistEventContactsRV.setAdapter(contactsRecyclerViewAdapter);
        checklistEventContactsRV.setLayoutManager(new LinearLayoutManager(this));
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
