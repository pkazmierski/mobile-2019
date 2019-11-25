package com.example.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.ChecklistEvent;
import com.example.kandydatpl.models.UserData;
import com.example.kandydatpl.utils.FileHelper;
import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.kandydatpl.logic.Logic.dataProvider;

@RequiresApi(api = Build.VERSION_CODES.N)
public class EventChecklistActivity extends NavigationDrawerActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<String> items;
    private RecyclerViewAdapter adapter;

    private List<ChecklistEvent> checklistEvents = new ArrayList<>();
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
        if (filterDate != null) {
            checklistEvents = checklistEvents.stream().filter(item -> item.getDeadline().after(filterDate)).collect(Collectors.toList());
        }

        recyclerView = findViewById(R.id.taskListView);
        floatingActionButton = findViewById(R.id.addTaskButton);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditListItemActivity.class);
            intent.putExtra("item", new ChecklistEvent());
            intent.putExtra("requestCode", newItemRequest);
            startActivityForResult(intent, newItemRequest);
        });

        items = FileHelper.readData(this);

        adapter = new RecyclerViewAdapter(checklistEvents, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(checklistEvents, position_dragged, position_target);
                adapter.notifyItemMoved(position_dragged, position_target);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int index = viewHolder.getAdapterPosition();
                adapter.deleteItem(index);
                //checklistEvents.remove(index);
                adapter.notifyItemRemoved(index);
                showUndoSnackbar();

            }
        });
        helper.attachToRecyclerView(recyclerView);
        dataProvider.getAllEvents(afterAllEventsSuccess, afterAllPublicEventsFailure, afterAllUserEventsFailure);
    }

    private Runnable afterAllEventsSuccess = () -> runOnUiThread(() -> {
        List<ChecklistEvent> checklistEvents = DataStore.getChecklistEvents();
        HashMap<ChecklistEvent, Integer> checkListEventsOrder = UserData.getInstance().getEventsOrder();
        for (ChecklistEvent event : checkListEventsOrder.keySet()) {
            if (!checklistEvents.contains(event))
                continue;
            else {
                int targetIndex = checkListEventsOrder.get(event);
                int sourceIndex = checklistEvents.indexOf(event);
                Collections.swap(checklistEvents, targetIndex, sourceIndex);
            }
        }
        adapter.notifyDataSetChanged();
    });

    private Runnable afterAllPublicEventsFailure = () -> runOnUiThread(() -> {
        Toast.makeText(this, "Public events fetch failed", Toast.LENGTH_SHORT).show();
    });

    private Runnable afterAllUserEventsFailure = () -> runOnUiThread(() -> {
        Toast.makeText(this, "User events fetch failed", Toast.LENGTH_SHORT).show();
    });


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
                    ChecklistEvent item = (ChecklistEvent) data.getSerializableExtra("newItem");
                    adapter.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == editItemRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ChecklistEvent item = (ChecklistEvent) data.getSerializableExtra("newItem");
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
