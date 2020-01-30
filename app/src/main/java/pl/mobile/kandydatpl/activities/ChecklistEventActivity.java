package pl.mobile.kandydatpl.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Toast;

import pl.mobile.kandydatpl.R;
import pl.mobile.kandydatpl.adapters.ChecklistEventRecyclerViewAdapter;
import pl.mobile.kandydatpl.data.DataStore;
import pl.mobile.kandydatpl.logic.Logic;
import pl.mobile.kandydatpl.models.ChecklistEvent;
import pl.mobile.kandydatpl.models.UserData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static pl.mobile.kandydatpl.logic.Logic.dataProvider;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChecklistEventActivity extends NavigationDrawerActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private static final String TAG = "ChecklistEventActivity";
    private List<String> items;
    private ChecklistEventRecyclerViewAdapter adapter;

    public static int newItemRequest = 1;
    public static int editItemRequest = 2;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ProgressDialog getUserDataDialog;

    private List<ChecklistEvent> checklistEvents = new ArrayList<>();
    private Date filterDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_checklist, null, false);
        drawer.addView(contentView, 0);

        if (!Logic.appSyncInitialized) {
            Logic.initAppSync(getApplicationContext());

            getUserDataDialog = new ProgressDialog(ChecklistEventActivity.this);

            getUserDataDialog.setMessage(getString(R.string.getting_user_data));
            getUserDataDialog.setTitle(getString(R.string.database_access));
            getUserDataDialog.setIndeterminate(true);
            getUserDataDialog.setCancelable(false);
            getUserDataDialog.show();

            Runnable getDataSuccess = () -> runOnUiThread(() -> {
                if (DataStore.getUserData() == null) {
                    dataProvider.createNewUserData(createUserDataSuccess, createUserDataFailure);
                    DataStore.setUserData(UserData.getInstance());
                    UserData.getInstance().setQuestionBookmarks(new ArrayList<>());
                    UserData.getInstance().setEventsOrder(new HashMap<>());
                } else {
                    getUserDataDialog.dismiss();
//                    hideSoftKeyBoard();
                    activityInit();
                }
            });

            dataProvider.getUserDataOnLogin(getDataSuccess, getUserDataFailure);
        } else {
            activityInit();
        }
    }

    private Runnable getUserDataFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.failed_to_get_user_data), Toast.LENGTH_LONG).show());

    private Runnable createUserDataSuccess = () -> runOnUiThread(() -> {
        getUserDataDialog.dismiss();
        activityInit();
        Toast.makeText(getApplicationContext(), getString(R.string.created_user_data), Toast.LENGTH_LONG).show();
    });

    private Runnable createUserDataFailure = () -> runOnUiThread(() ->
            Toast.makeText(getApplicationContext(), getString(R.string.failed_to_create_user_data), Toast.LENGTH_LONG).show());

    private void activityInit() {
        Intent dateFilterIntent = getIntent();
        filterDate = (Date) dateFilterIntent.getSerializableExtra("filterDate");

        recyclerView = findViewById(R.id.taskListView);
        floatingActionButton = findViewById(R.id.addTaskButton);
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddOrEditChecklistEventActivity.class);
            intent.putExtra("item", new ChecklistEvent());
            intent.putExtra("requestCode", newItemRequest);
            startActivityForResult(intent, newItemRequest);
        });

        adapter = new ChecklistEventRecyclerViewAdapter(checklistEvents, this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder dragged, @NonNull RecyclerView.ViewHolder target) {

//                int position_dragged = dragged.getAdapterPosition();
//                int position_target = target.getAdapterPosition();
//
//                Collections.swap(DataStore.getAllChecklistEvents(), position_dragged, position_target);
//                dataProvider.setEventsOrder(null, afterSetEventsOrderFailure, adapter.getEventsOrder());
//                Log.d(TAG, "onMove: " + adapter.getEventsOrder().toString());
//                adapter.notifyItemMoved(position_dragged, position_target);
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int index = viewHolder.getAdapterPosition();
                if (!adapter.getItemFromList(index).isUserCreated()) {
                    Toast.makeText(recyclerView.getContext(), getString(R.string.you_cannot_delete_system_events), Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    return;
                }
                adapter.deleteItem(index);
                //checklistEvents.remove(index);
                adapter.notifyItemRemoved(index);
                showUndoSnackbar();
            }
        });
        helper.attachToRecyclerView(recyclerView);

        dataProvider.getAllEvents(afterAllEventsSuccess, afterAllPublicEventsFailure, afterAllUserEventsFailure);
    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private Runnable afterSetEventsOrderFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, getString(R.string.cannot_save_the_new_events_order), Toast.LENGTH_SHORT).show());

    private Runnable afterAllEventsSuccess = () -> runOnUiThread(() -> {
        List<ChecklistEvent> checklistEvents = DataStore.getAllChecklistEvents();
//        HashMap<String, Integer> checkListEventsOrder = UserData.getInstance().getEventsOrder();

        Collections.sort(checklistEvents, new Comparator<ChecklistEvent>() {
            @Override
            public int compare(ChecklistEvent lhs, ChecklistEvent rhs) {
                return lhs.getDeadline().compareTo(rhs.getDeadline()) > 0 ? 1 : -1;
            }
        }); //first, sort by date

//        for (String eventId : checkListEventsOrder.keySet()) {
//            ChecklistEvent event = null;
//            for (ChecklistEvent tempEvent : checklistEvents) { //find the event by ID
//                if (tempEvent.getId().equals(eventId))
//                    event = tempEvent;
//            }
//            if (event != null) {
//                //propely place those events that exist in order list and event list
//                int targetIndex = checkListEventsOrder.get(event.getId());
//                if (targetIndex >= checklistEvents.size()) {
//                    dataProvider.setEventsOrder(null, afterSetEventsOrderFailure, adapter.getEventsOrder());
//                    break;
//                }
//                int sourceIndex = checklistEvents.indexOf(event);
//                Collections.swap(checklistEvents, targetIndex, sourceIndex);
//            } else {
//                //if the event does not exist in the event array, it's desynchronized, so force sync
//                dataProvider.setEventsOrder(null, afterSetEventsOrderFailure, adapter.getEventsOrder());
//                break;
//            }
//        }

        List<ChecklistEvent> filteredChecklistEvents;
        if (filterDate != null) {
            filteredChecklistEvents = DataStore.getAllChecklistEvents().stream().filter(item -> {
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(item.getDeadline());
                cal2.setTime(filterDate);
                return cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                        cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
            }).collect(Collectors.toList());
        } else {
            filteredChecklistEvents = DataStore.getAllChecklistEvents();
        }

        List<ChecklistEvent> toRemove = new ArrayList<>();

        for (ChecklistEvent ev : filteredChecklistEvents) {
            if (!ev.isUserCreated() && ev.getOfferId() != null && !DataStore.getUserData().getActiveOffersIds().contains(ev.getOfferId())) {
                toRemove.add(ev);
            }
        }

        filteredChecklistEvents.removeAll(toRemove);

        adapter.setChecklistEvents(filteredChecklistEvents);

        adapter.notifyDataSetChanged();
    });

    private Runnable afterAllPublicEventsFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, getString(R.string.public_events_fetch_failed), Toast.LENGTH_SHORT).show());

    private Runnable afterAllUserEventsFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, getString(R.string.user_events_fetch_failed), Toast.LENGTH_SHORT).show());

    private Runnable afterAddEventSuccess = () -> runOnUiThread(() -> {
        Toast.makeText(this, getString(R.string.item_added), Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    });

    private Runnable afterEditEventSuccess = () -> runOnUiThread(() -> {
        Toast.makeText(this, getString(R.string.item_changed), Toast.LENGTH_SHORT).show();
        dataProvider.getAllEvents(afterAllEventsSuccess, afterAllPublicEventsFailure, afterAllUserEventsFailure);
    });

    private Runnable afterRemoveEventSuccess = () -> runOnUiThread(() -> {
        adapter.notifyDataSetChanged();
    });

    private Runnable afterRemoveEventFailure = () -> runOnUiThread(() -> {
        Toast.makeText(this, getString(R.string.item_removal_failed), Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    });

    private Runnable afterAddEventFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, getString(R.string.failed_to_add_the_item), Toast.LENGTH_SHORT).show());

    private Runnable afterEditEventFailure = () -> runOnUiThread(() ->
            Toast.makeText(this, getString(R.string.failed_to_edit_the_item), Toast.LENGTH_SHORT).show());

    private void showUndoSnackbar() {
        View view = recyclerView.findViewById(R.id.parent_layout);
        Snackbar snackbar = Snackbar.make(view, R.string.snack_bar_text,
                Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snack_bar_undo, v -> adapter.undoDelete());
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    dataProvider.removeSingleUserEvent(afterRemoveEventSuccess, afterRemoveEventFailure, adapter.getRecentlyDeletedItem());
                    dataProvider.setEventsOrder(null, afterSetEventsOrderFailure, adapter.getEventsOrder());
                }
            }

        });
        snackbar.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == newItemRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ChecklistEvent item = (ChecklistEvent) data.getSerializableExtra("newItem");
                    dataProvider.createSingleUserEvent(afterAddEventSuccess, afterAddEventFailure, item);
                }
            }
        } else if (requestCode == editItemRequest) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    ChecklistEvent item = (ChecklistEvent) data.getSerializableExtra("newItem");
                    //int index = data.getIntExtra("index", -1);
                    dataProvider.updateSingleUserEvent(afterEditEventSuccess, afterEditEventFailure, item);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        items.remove(position);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, getString(R.string.delete), Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {

    }
}
