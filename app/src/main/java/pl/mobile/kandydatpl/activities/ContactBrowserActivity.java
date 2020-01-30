package pl.mobile.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import pl.mobile.kandydatpl.R;
import pl.mobile.kandydatpl.adapters.ContactsRecyclerViewAdapter;
import pl.mobile.kandydatpl.data.DataStore;
import pl.mobile.kandydatpl.models.Contact;
import pl.mobile.kandydatpl.models.File;

import static pl.mobile.kandydatpl.logic.Logic.dataProvider;

public class ContactBrowserActivity extends NavigationDrawerActivity {

    RecyclerView recyclerView;
    ContactsRecyclerViewAdapter adapter;
    EditText filterContactsInputTxt;
    ArrayList<Contact> contacts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_contact_browser);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_contact_browser, null, false);
        drawer.addView(contentView, 0);

        filterContactsInputTxt = findViewById(R.id.filterContactsInputTxt);

        filterContactsInputTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        initRecyclerView();

        dataProvider.getContacts(getContactsSuccess, getContactsFailure);
    }

    private void filter(String text) {
        ArrayList<Contact> filteredContacts = new ArrayList<Contact>();

        for (Contact contact : contacts) {
            if(contact.getName().toLowerCase().contains(text.toLowerCase()))
                filteredContacts.add(contact);
        }

        adapter.filterList(filteredContacts);
    }

    private Runnable getContactsSuccess = () -> runOnUiThread(() -> {
        contacts = DataStore.getContacts();
        adapter.filterList(contacts);
    });

    private Runnable getContactsFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.get_contacts_failed), Toast.LENGTH_LONG).show());

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.contactsRecyclerView);
        adapter = new ContactsRecyclerViewAdapter(this, contacts);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
