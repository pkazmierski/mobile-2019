package pl.mobile.kandydatpl.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import pl.mobile.kandydatpl.activities.AddOrEditChecklistEventActivity;
import pl.mobile.kandydatpl.activities.ChecklistEventActivity;
import pl.mobile.kandydatpl.models.ChecklistEvent;
import pl.mobile.kandydatpl.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChecklistEventRecyclerViewAdapter extends RecyclerView.Adapter<ChecklistEventRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ChecklistEventRVAdapter";
    private List<ChecklistEvent> checklistEvents = new ArrayList<>();
    private Activity context;
    private SimpleDateFormat formatter;

    private ChecklistEvent recentlyDeletedItem;
    private int recentlyDeletedItemPosition;

    public List<ChecklistEvent> getChecklistEvents() {
        return checklistEvents;
    }

    public void setChecklistEvents(List<ChecklistEvent> checklistEvents) {
        this.checklistEvents = checklistEvents;
    }

    public ChecklistEventRecyclerViewAdapter(List<ChecklistEvent> checklistEvents, Activity context) {
        this.checklistEvents = checklistEvents;
        this.context = context;
    }

    public void refresh(List<ChecklistEvent> listItemTexts){
        this.checklistEvents = listItemTexts;
        notifyDataSetChanged();
    }

    public void add(ChecklistEvent newItem) {
        checklistEvents.add(newItem);
        notifyDataSetChanged();
    }

    public void remove(int position) {
        checklistEvents.remove(position);
        //notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void edit(ChecklistEvent item, int index) {
        checklistEvents.set(index, item);
        notifyDataSetChanged();
    }

    public ChecklistEvent getItemFromList(int index) {
        return checklistEvents.get(index);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        ChecklistEvent item = checklistEvents.get(i);
        viewHolder.listItem.setText(item.getTitle());

        viewHolder.dateDisplay.setText(formatter.format(item.getDeadline().getTime()));


        viewHolder.listItemCheckbox.setChecked(item.isDone());
        viewHolder.listItemCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setDone(isChecked);
            //notifyDataSetChanged();
            if (isChecked) {
                viewHolder.listItem.setPaintFlags(viewHolder.listItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.cardViewItem.setCardBackgroundColor(Color.parseColor("#FAFAFA"));
                viewHolder.listItem.setTextColor(Color.parseColor("gray"));
                viewHolder.dateDisplay.setTextColor(Color.parseColor("gray"));
                viewHolder.dateDisplay.setPaintFlags(viewHolder.listItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.cardViewItem.setCardElevation(3);
            } else {
                viewHolder.listItem.setPaintFlags(viewHolder.listItem.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                viewHolder.cardViewItem.setCardBackgroundColor(viewHolder.defaultColor);
                viewHolder.dateDisplay.setPaintFlags(viewHolder.listItem.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                viewHolder.listItem.setTextColor(Color.parseColor("black"));
                viewHolder.dateDisplay.setTextColor(Color.parseColor("black"));
                viewHolder.cardViewItem.setCardElevation(8);
            }
        });

//        viewHolder.editItem.setOnClickListener(v -> {
//            Intent intent = new Intent(context, AddOrEditChecklistEventActivity.class);
//            intent.putExtra("index", viewHolder.getAdapterPosition());
//            intent.putExtra("item", item);
//            context.startActivityForResult(intent, ChecklistEventActivity.editItemRequest);
//        });

        viewHolder.view.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddOrEditChecklistEventActivity.class);
            intent.putExtra("index", viewHolder.getAdapterPosition());
            intent.putExtra("item", item);
            intent.putExtra("requestCode", ChecklistEventActivity.editItemRequest);
            context.startActivityForResult(intent, ChecklistEventActivity.editItemRequest);
        });
    }

    public void deleteItem(int position) {
        recentlyDeletedItem = checklistEvents.get(position);
        recentlyDeletedItemPosition = position;
        checklistEvents.remove(position);
        notifyItemRemoved(position);
    }

    public void undoDelete() {
        checklistEvents.add(recentlyDeletedItemPosition,
                recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition);
    }

    @Override
    public int getItemCount() {
        return checklistEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardViewItem;
        TextView listItem;
        CheckBox listItemCheckbox;
        Button editItem;
        View view;
        TextView dateDisplay;
        ColorStateList defaultColor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listItem = itemView.findViewById(R.id.itemText);
            listItemCheckbox = itemView.findViewById(R.id.itemCheckBox);
            //editItem = itemView.findViewById(R.id.editItemButton);
            dateDisplay = itemView.findViewById(R.id.dateDisplay);
            cardViewItem = itemView.findViewById(R.id.parent_layout);
            defaultColor = cardViewItem.getCardBackgroundColor();
            this.view = itemView;
        }
    }


    public void setChecklistEvents() {

    }

    public HashMap<String, Integer> getEventsOrder() {
        //TODO implement @Piotr Kocik
        //TODO trzeba też dodać synchronizację po każdej zmianie kolejności:
        // dataProvider.setEvents(null, <odpowiednie runnable>, adapter.getEventsOrder()) <- wywołane w kodzie gdzie przeniesiony jest element
        // Runnable z toastem, że się nie udało zsynchronizować jest w ChecklistEventActivity, to skopiuj sobie
        // jak dodajemy nowy event do checklisty, to trzeba dodać go do bazy danych dataprovider.createsingleuserevent
        // skoro jest możliwość przywracania, to request do bazy musi nastąpić po zniknięciu snackbara, funkcja na usuwanie bedzie
        // zablokowanie edycji eventów z bazy
        // string to id, integer to pozycja
        // jeśli chcemy rozpoznawać, czy przyszliśmy z kalendarza (po filtracji) to checemy uniemożliwić zmianę kolejności

        HashMap<String, Integer> changedOrderMap = new HashMap<>();

        for (ChecklistEvent event : checklistEvents) {
            changedOrderMap.put(event.getId(), checklistEvents.indexOf(event));
        }
        return changedOrderMap;
    }

    public ChecklistEvent getRecentlyDeletedItem() {
        return recentlyDeletedItem;
    }
}
