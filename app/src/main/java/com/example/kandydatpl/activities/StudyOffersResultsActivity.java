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
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.StudyOffer;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import static com.example.kandydatpl.logic.Logic.dataProvider;

public class StudyOffersResultsActivity extends NavigationDrawerActivity {
    private static final String TAG = "StudyOffersResultsActiv";
    private ArrayList<String> tagsToFind;
    private ArrayList<String> matchingOffersTitles = new ArrayList<>();
    private LinkedHashSet<StudyOffer> matchingOffers;
    private ListView studyOffersResultsListView;
    private ArrayAdapter<String> arrayAdapter;

    private boolean initCompleted = false;

    private boolean showOnlyActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_study_offers_results, null, false);
        drawer.addView(contentView, 0);

        studyOffersResultsListView = findViewById(R.id.studyOffersResultsListView);

        Intent intent = getIntent();
        showOnlyActive = intent.getBooleanExtra("showOnlyActive", false);

        matchingOffers = new LinkedHashSet<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, matchingOffersTitles);
        if (!showOnlyActive) {
            tagsToFind = getIntent().getStringArrayListExtra("tagsToFind");
            Log.d(TAG, "Received tags: " + tagsToFind);

            for (String tag : tagsToFind) {
                matchingOffers.addAll(DataStore.getOffersWithTag(tag));
            }

            for (StudyOffer studyOffer : matchingOffers) {
                matchingOffersTitles.add(studyOffer.getTitle());
            }
            arrayAdapter.notifyDataSetChanged();

            studyOffersResultsListView.setOnItemClickListener((parent, view, position, id) -> {
                ArrayList<StudyOffer> ofs = new ArrayList<>(matchingOffers);
                startActivity(new Intent(this, StudyOfferDetailsActivity.class).putExtra("offerIndex", DataStore.getStudyOffers().indexOf(ofs.get(position))));
                Log.d(TAG, "clicked offer in the list: " + ofs.get(position).getTitle());

//            Object o = studyOffersResultsListView.getItemAtPosition(position);
//            Log.d(TAG, "clicked offer in the list: " + (String)o);
            });
            studyOffersResultsListView.setAdapter(arrayAdapter);
        } else {
            toolbar.setTitle("Your active offers");
            dataProvider.getStudyOffers(afterStudyOffersSuccess, afterStudyOffersFailure);
        }


    }

    private Runnable afterStudyOffersFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, getString(R.string.publi_events_fetch_failed), Toast.LENGTH_SHORT).show());

    private Runnable afterStudyOffersSuccess = () -> {
        matchingOffers.clear();
        for (StudyOffer studyOffer : DataStore.getStudyOffers()) {
            if (DataStore.getUserData().getActiveOffersIds().contains(studyOffer.getId()))
                matchingOffers.add(studyOffer);
        }

        Log.d(TAG, "onCreate: DataStore.getStudyOffers(): " + DataStore.getStudyOffers());
        Log.d(TAG, "onCreate: matchingOffers: " + matchingOffers);

        matchingOffersTitles.clear();
        for (StudyOffer studyOffer : matchingOffers) {
            matchingOffersTitles.add(studyOffer.getTitle());
        }

        Log.d(TAG, "onCreate: matchingOffersTitles: " + matchingOffersTitles);

        runOnUiThread(() -> {
            arrayAdapter.notifyDataSetChanged();

            studyOffersResultsListView.setOnItemClickListener((parent, view, position, id) -> {
                ArrayList<StudyOffer> ofs = new ArrayList<>(matchingOffers);
                startActivity(new Intent(this, StudyOfferDetailsActivity.class).putExtra("offerIndex", DataStore.getStudyOffers().indexOf(ofs.get(position))));
                Log.d(TAG, "clicked offer in the list: " + ofs.get(position).getTitle());
            });
            studyOffersResultsListView.setAdapter(arrayAdapter);
        });

        initCompleted = true;
    };

    @Override
    public void onResume() {
        super.onResume();
        if (initCompleted) {
            afterStudyOffersSuccess.run();
        }
    }
}
