package com.example.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.kandydatpl.R;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.StudyOffer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class StudyOffersResultsActivity extends NavigationDrawerActivity {
    private static final String TAG = "StudyOffersResultsActiv";
    ArrayList<String> tagsToFind;
    ArrayList<String> matchingOffersTitles = new ArrayList<>();
    LinkedHashSet<StudyOffer> matchingOffers;
    ListView studyOffersResultsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_study_offers_results, null, false);
        drawer.addView(contentView, 0);

        studyOffersResultsListView = findViewById(R.id.studyOffersResultsListView);
        tagsToFind = getIntent().getStringArrayListExtra("tagsToFind");
        Log.d(TAG, "Received tags: " + tagsToFind);

        matchingOffers = new LinkedHashSet<>();
        for (String tag : tagsToFind) {
            matchingOffers.addAll(DataStore.getOffersWithTag(tag));
        }

        for (StudyOffer studyOffer : matchingOffers) {
            matchingOffersTitles.add(studyOffer.getTitle());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, matchingOffersTitles);

        studyOffersResultsListView.setOnItemClickListener((parent, view, position, id) -> {
            ArrayList<StudyOffer> ofs = new ArrayList<>(matchingOffers);
            startActivity(new Intent(this, StudyOfferDetailsActivity.class).putExtra("offerIndex", DataStore.getStudyOffers().indexOf(ofs.get(position))));
            Log.d(TAG, "clicked offer in the list: " + ofs.get(position).getTitle());

//            Object o = studyOffersResultsListView.getItemAtPosition(position);
//            Log.d(TAG, "clicked offer in the list: " + (String)o);
        });

        studyOffersResultsListView.setAdapter(arrayAdapter);
    }
}
