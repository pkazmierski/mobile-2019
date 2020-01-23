package com.example.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.FilesRecyclerViewAdapter;
import com.example.kandydatpl.adapters.QuestionsRecyclerViewAdapter;
import com.example.kandydatpl.data.DataStore;

import java.util.ArrayList;

import static com.example.kandydatpl.logic.Logic.dataProvider;

public class FileBrowserActivity extends NavigationDrawerActivity {

    RecyclerView recyclerView;
    FilesRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_file_browser);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_file_browser, null, false);
        drawer.addView(contentView, 0);

        dataProvider.getFiles(getFilesSuccess, getFilesFailure);

        initRecyclerView();
    }

    private Runnable getFilesSuccess = () -> runOnUiThread(() -> {
        adapter.notifyDataSetChanged();
    });

    private Runnable getFilesFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.get_files_failed), Toast.LENGTH_LONG).show());

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.filesRecyclerView);
        adapter = new FilesRecyclerViewAdapter(this, DataStore.getFiles());

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
