package com.example.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.kandydatpl.R;
import com.example.kandydatpl.adapters.ChecklistEventRecyclerViewAdapter;
import com.example.kandydatpl.data.DataStore;
import com.example.kandydatpl.models.ChecklistEvent;
import com.example.kandydatpl.models.UserData;
import com.example.kandydatpl.utils.FileHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.kandydatpl.logic.Logic.dataProvider;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChecklistEventActivity extends NavigationDrawerActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<String> items;
    private ChecklistEventRecyclerViewAdapter adapter;

    private List<ChecklistEvent> checklistEvents = new ArrayList<>();
    public static int newItemRequest = 1;
    public static int editItemRequest = 2;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;

    public ChecklistEventActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_checklist);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_checklist, null, false);
        drawer.addView(contentView, 0);

        Intent dateFilterIntent = getIntent();
        Date filterDate = (Date) dateFilterIntent.getSerializableExtra("filterDate");
        if (filterDate != null) {
            checklistEvents = checklistEvents.stream().filter(item -> item.getDeadline().after(filterDate)).collect(Collectors.toList());
        }

        recyclerView = findViewById(R.id.taskListView);
        floatingActionButton = findViewById(R.id.addTaskButton);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddOrEditChecklistEventActivity.class);
            intent.putExtra("item", new ChecklistEvent());
            intent.putExtra("requestCode", newItemRequest);
            startActivityForResult(intent, newItemRequest);
        });

        items = FileHelper.readData(this);

        adapter = new ChecklistEventRecyclerViewAdapter(checklistEvents, this);
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

    private Runnable afterSetEventsOrderFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, "Cannot save the new order of the checklist", Toast.LENGTH_SHORT).show());

    private Runnable afterAllEventsSuccess = () -> runOnUiThread(() -> {
        List<ChecklistEvent> checklistEvents = DataStore.getChecklistEvents();
        HashMap<String, Integer> checkListEventsOrder = UserData.getInstance().getEventsOrder();

        Collections.sort(checklistEvents, new Comparator<ChecklistEvent>() {
            @Override
            public int compare(ChecklistEvent lhs, ChecklistEvent rhs) {
                return lhs.getDeadline().compareTo(rhs.getDeadline()) > 0 ? -1 : 1;
            }
        }); //first, sort by date

        for (String eventId : checkListEventsOrder.keySet()) {
            ChecklistEvent event = null;
            for (ChecklistEvent tempEvent : checklistEvents) { //find the event by ID
                if (tempEvent.getId().equals(eventId))
                    event = tempEvent;
            }


            if (event != null) {
                //propely place those events that exist in order list and event list
                int targetIndex = checkListEventsOrder.get(event);
                int sourceIndex = checklistEvents.indexOf(event);
                Collections.swap(checklistEvents, targetIndex, sourceIndex);
            } else {
                //if the event does not exist in the event array, it's desynchronized, so force sync
                dataProvider.setEventsOrder(null, afterSetEventsOrderFailure, adapter.getEventsOrder());
            }
        }
        adapter.notifyDataSetChanged();
    });

    private Runnable afterAllPublicEventsFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, "Public events fetch failed", Toast.LENGTH_SHORT).show());

    private Runnable afterAllUserEventsFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, "User events fetch failed", Toast.LENGTH_SHORT).show());


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
