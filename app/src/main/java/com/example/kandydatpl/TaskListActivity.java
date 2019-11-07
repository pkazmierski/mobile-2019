package com.example.kandydatpl;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TaskListActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText itemET;
    private Button btn;
    private List<String> items;
    private RecyclerViewAdapter adapter;

    private List<ChecklistItem> testArray;
    public static int newItemRequest = 1;
    public static int editItemRequest = 2;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ChecklistItem mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public TaskListActivity() {
        testArray = new ArrayList<>();
        testArray.add(new ChecklistItem("FirstTask", "", false, false, new Date()));
        testArray.add(new ChecklistItem("Second", false));
        Calendar cal = Calendar.getInstance();
        cal.set(2019, 10, 6);
        testArray.add(new ChecklistItem("Third", "Description", false, cal.getTime()));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);

        Intent dateFilterIntent = getIntent();
        Date filterDate = (Date) dateFilterIntent.getSerializableExtra("filterDate");
        if(filterDate != null){
            testArray = testArray.stream().filter(item -> item.getDeadline().after(filterDate)).collect(Collectors.toList());
        }

        recyclerView = findViewById(R.id.taskListView);
        floatingActionButton = findViewById(R.id.addTaskButton);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEditListItemActivity.class);
            intent.putExtra("item", new ChecklistItem());
            intent.putExtra("requestCode", newItemRequest);
            startActivityForResult(intent, newItemRequest);
        });

        items = FileHelper.readData(this);

        adapter = new RecyclerViewAdapter(testArray, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

                int position_dragged = dragged.getAdapterPosition();
                int position_target = target.getAdapterPosition();

                Collections.swap(testArray, position_dragged, position_target);
                adapter.notifyItemMoved(position_dragged, position_target);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int index = viewHolder.getAdapterPosition();
                adapter.deleteItem(index);
                //testArray.remove(index);
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
                    ChecklistItem item = (ChecklistItem) data.getSerializableExtra("newItem");
                    adapter.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == editItemRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ChecklistItem item = (ChecklistItem) data.getSerializableExtra("newItem");
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
        //FileHelper.writeData(items, this);
        Toast.makeText(this, "Delete", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {

    }
}
