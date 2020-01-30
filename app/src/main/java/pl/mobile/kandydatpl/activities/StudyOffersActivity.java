package pl.mobile.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.allyants.chipview.ChipView;

import pl.mobile.kandydatpl.data.AppSyncDb;
import pl.mobile.kandydatpl.data.DataStore;
import pl.mobile.kandydatpl.models.StudyOffer;
import pl.mobile.kandydatpl.utils.CustomChipAdapter;
import pl.mobile.kandydatpl.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashSet;

public class StudyOffersActivity extends NavigationDrawerActivity {
    private static final String TAG = "StudyOffersActivity";
    ChipView cvTag;
    com.allyants.chipview.ChipAdapter adapter;

    LinkedHashSet<String> tags = new LinkedHashSet<>();
    ArrayList<Object> searchData = new ArrayList<>();

    private Runnable getStudyOffersSuccess = () -> {
//        Log.d(TAG, "DataStore.getStudyOffers(): " + DataStore.getStudyOffers());
        for (StudyOffer so : DataStore.getStudyOffers()) {
            tags.addAll(so.getTags());
        }
        searchData = new ArrayList<>(tags);
//        Log.d(TAG, "searchData: " + searchData);
        runOnUiThread(() -> {
            adapter = new CustomChipAdapter(searchData);
            cvTag.setAdapter(adapter);

            Field f = null; //NoSuchFieldException
            try {
                f = cvTag.getClass().getDeclaredField("etSearch");
                f.setAccessible(true);
                EditText etSearch = (EditText) f.get(cvTag); //IllegalAccessException
                String searchPrompt = getResources().getString(R.string.search);
                etSearch.setHint(searchPrompt);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

        });
    };

    private Runnable getStudyOffersFailure = () -> {
        Toast.makeText(this, getString(R.string.failed_to_retrieve_study_offers), Toast.LENGTH_SHORT).show();
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_study_offers, null, false);
        drawer.addView(contentView, 0);

        AppSyncDb.getInstance().getStudyOffers(getStudyOffersSuccess, getStudyOffersFailure);

        cvTag = (ChipView) findViewById(R.id.cvTag);
    }

    public void searchStudyOffersBtn(View view) {
        ArrayList<String> selectedTags = new ArrayList<>();
        for (int i = 0; i < searchData.size(); i++) {
            if (adapter.isSelected(i))
                selectedTags.add((String) searchData.get(i));
        }
//        Log.d(TAG, "searchStudyOffersBtn: " + selectedTags.toString());
        Intent intent = new Intent(this, StudyOffersResultsActivity.class);
        intent.putStringArrayListExtra("tagsToFind", (ArrayList<String>) selectedTags);
        startAnimatedActivity(intent);
    }
}
