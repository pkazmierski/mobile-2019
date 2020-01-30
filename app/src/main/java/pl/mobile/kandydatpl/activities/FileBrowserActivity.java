package pl.mobile.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pl.mobile.kandydatpl.R;
import pl.mobile.kandydatpl.adapters.FilesRecyclerViewAdapter;
import pl.mobile.kandydatpl.data.DataStore;
import pl.mobile.kandydatpl.models.File;
import pl.mobile.kandydatpl.models.Question;

import static pl.mobile.kandydatpl.logic.Logic.dataProvider;

public class FileBrowserActivity extends NavigationDrawerActivity {

    RecyclerView recyclerView;
    FilesRecyclerViewAdapter adapter;
    ArrayList<File> files = new ArrayList<>();
    EditText filterFilesInputTxt;

    private static final String TAG = "FileBrowserActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_file_browser);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_file_browser, null, false);
        drawer.addView(contentView, 0);

        filterFilesInputTxt = findViewById(R.id.filterFilesInputTxt);

        filterFilesInputTxt.addTextChangedListener(new TextWatcher() {
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

        dataProvider.getFiles(getFilesSuccess, getFilesFailure);
    }

    private void filter(String text) {
        ArrayList<File> filteredFiles = new ArrayList<File>();

        for (File file : files) {
            if(file.getName().toLowerCase().contains(text.toLowerCase()))
                filteredFiles.add(file);
        }

        adapter.filterList(filteredFiles);
    }

    private Runnable getFilesSuccess = () -> runOnUiThread(() -> {
        files = DataStore.getFiles();
        adapter.filterList(files);
    });

    private Runnable getFilesFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.get_files_failed), Toast.LENGTH_LONG).show());

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.filesRecyclerView);
        adapter = new FilesRecyclerViewAdapter(this, files);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
