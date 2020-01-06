package com.example.kandydatpl.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.StudyOffer;

public class StudyOfferDetailsActivity extends AppCompatActivity {

    int offerIndex;
    StudyOffer studyOffer;

    TextView studyOfferTitle, studyOfferContent;
    Button addToActiveOffersBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_offer_details);

        studyOfferTitle = findViewById(R.id.studyOfferTitle);
        studyOfferContent = findViewById(R.id.studyOfferContent);
        addToActiveOffersBtn = findViewById(R.id.addToActiveOffersBtn);

        Intent thisIntent = getIntent();
        offerIndex = thisIntent.getIntExtra("offerIndex", 0);

        studyOffer = DataStore.getStudyOffers().get(offerIndex);

        studyOfferTitle.setText(studyOffer.getTitle());
        studyOfferContent.setText(studyOffer.getContent());
    }

    public void addToActiveOffersBtn(View view) {
        //todo implement
        Toast.makeText(this, "Added to active offers", Toast.LENGTH_SHORT).show();
    }
}
