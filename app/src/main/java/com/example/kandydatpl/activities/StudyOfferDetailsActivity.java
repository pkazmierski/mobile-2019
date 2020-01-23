package com.example.kandydatpl.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.data.AppSyncDb;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.StudyOffer;

public class StudyOfferDetailsActivity extends AppCompatActivity {

    int offerIndex;
    StudyOffer studyOffer;

    TextView studyOfferTitle, studyOfferContent;
    Button addToActiveOffersBtn;
    Context ctx;
    boolean adding = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_offer_details);

        ctx = this;

        studyOfferTitle = findViewById(R.id.studyOfferTitle);
        studyOfferContent = findViewById(R.id.studyOfferContent);
        addToActiveOffersBtn = findViewById(R.id.addToActiveOffersBtn);

        Intent thisIntent = getIntent();
        offerIndex = thisIntent.getIntExtra("offerIndex", 0);

        studyOffer = DataStore.getStudyOffers().get(offerIndex);

        studyOfferTitle.setText(studyOffer.getTitle());
        studyOfferContent.setText(studyOffer.getContent());

        if (DataStore.getUserData().getActiveOffersIds().contains(studyOffer.getId())) {
            addToActiveOffersBtn.setText(getString(R.string.remove_from_active_offers));
            adding = false;
        }
    }

    public void addToActiveOffersBtn(View view) {
        Runnable addedToActiveOffersSuccess = () -> runOnUiThread(() -> {
            Toast.makeText(ctx, getString(R.string.added_to_active_offers), Toast.LENGTH_SHORT).show();
            finish();
        });
        Runnable addedToActiveOffersFailure = () -> runOnUiThread(() -> Toast.makeText(ctx, getString(R.string.failed_to_add_to_active_offers), Toast.LENGTH_SHORT).show());

        Runnable removedFromActiveOffersSuccess = () -> runOnUiThread(() -> {
            Toast.makeText(ctx, getString(R.string.removed_from_active_offers), Toast.LENGTH_SHORT).show();
            finish();
        });
        Runnable removedFromActiveOffersFailure = () -> runOnUiThread(() -> Toast.makeText(ctx, getString(R.string.failed_to_remove_from_active_offers), Toast.LENGTH_SHORT).show());

        if (adding)
            AppSyncDb.getInstance().switchOfferStatus(addedToActiveOffersSuccess, addedToActiveOffersFailure, studyOffer);
        else
            AppSyncDb.getInstance().switchOfferStatus(removedFromActiveOffersSuccess, removedFromActiveOffersFailure, studyOffer);

    }
}
