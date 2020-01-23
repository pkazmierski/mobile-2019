package com.example.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.ContactsRecyclerViewAdapter;
import com.example.kandydatpl.data.DataStore;

import static com.example.kandydatpl.logic.Logic.dataProvider;

public class ContactBrowserActivity extends NavigationDrawerActivity {

    RecyclerView recyclerView;
    ContactsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_contact_browser);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_contact_browser, null, false);
        drawer.addView(contentView, 0);

        dataProvider.getContacts(getContactsSuccess, getContactsFailure);

        initRecyclerView();
    }

    private Runnable getContactsSuccess = () -> runOnUiThread(() -> {
        adapter.notifyDataSetChanged();
    });

    private Runnable getContactsFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.get_contacts_failed), Toast.LENGTH_LONG).show());

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.contactsRecyclerView);
        adapter = new ContactsRecyclerViewAdapter(this, DataStore.getContacts());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
