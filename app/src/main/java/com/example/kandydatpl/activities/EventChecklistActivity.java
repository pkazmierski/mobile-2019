package com.example.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.kandydatpl.models.Event;
import com.example.kandydatpl.utils.FileHelper;
import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class EventChecklistActivity extends NavigationDrawerActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<String> items;
    private RecyclerViewAdapter adapter;

    private List<Event> events = new ArrayList<>();
    public static int newItemRequest = 1;
    public static int editItemRequest = 2;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    public EventChecklistActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_task_list);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_task_list, null, false);
        drawer.addView(contentView, 0);

        Intent dateFilterIntent = getIntent();
        Date filterDate = (Date) dateFilterIntent.getSerializableExtra("filterDate");
        if(filterDate != null){
            events = events.stream().filter(item -> item.getDeadline().after(filterDate)).collect(Collectors.toList());
        }

        recyclerView = findViewById(R.id.taskListView);
        floatingActionButton = findViewById(R.id.addTaskButton);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditListItemActivity.class);
            intent.putExtra("item", new Event());
            intent.putExtra("requestCode", newItemRequest);
            startActivityForResult(intent, newItemRequest);
        });

        items = FileHelper.readData(this);

        adapter = new RecyclerViewAdapter(events, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(events, position_dragged, position_target);
                adapter.notifyItemMoved(position_dragged, position_target);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int index = viewHolder.getAdapterPosition();
                adapter.deleteItem(index);
                //events.remove(index);
                adapter.notifyItemRemoved(index);
                showUndoSnackbar();

            }
        });
        helper.attachToRecyclerView(recyclerView);
    }

    private void showUndoSnackbar() {
        View view = recyclerView.findViewById(R.id.parent_layout);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> adapter.undoDelete());
        snackbar.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == newItemRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Event item = (Event) data.getSerializableExtra("newItem");
                    adapter.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == editItemRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Event item = (Event) data.getSerializableExtra("newItem");
                    int index = data.getIntExtra("index", -1);
                    adapter.edit(item, index);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        items.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {

    }
}
