package com.example.kandydatpl.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.kandydatpl.R;
import com.example.kandydatpl.activities.AddOrEditChecklistEventActivity;
import com.example.kandydatpl.activities.ChecklistEventActivity;
import com.example.kandydatpl.models.ChecklistEvent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChecklistEventRecyclerViewAdapter extends RecyclerView.Adapter<ChecklistEventRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "ChecklistEventRVAdapter";
    private List<ChecklistEvent> listItemTexts = new ArrayList<>();
    private Activity context;
    private SimpleDateFormat formatter;
    private ChecklistEvent recentlyDeletedItem;
    private int recentlyDeletedItemPosition;

    public ChecklistEventRecyclerViewAdapter(List<ChecklistEvent> listItemTexts, Activity context) {
        this.listItemTexts = listItemTexts;
        this.context = context;
    }

    public void add(ChecklistEvent newItem){
        listItemTexts.add(newItem);
        notifyDataSetChanged();
    }

    public void remove(int position){
        listItemTexts.remove(position);
        notifyDataSetChanged();
    }

    public void edit(ChecklistEvent item, int index){
        listItemTexts.set(index, item);
        notifyDataSetChanged();
    }

    public ChecklistEvent getItemFromList(int index){
        return listItemTexts.get(index);
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
        Log.d(TAG, "onBindViewHolder: called");

        ChecklistEvent item = listItemTexts.get(i);
        viewHolder.listItem.setText(item.getTitle());

        viewHolder.dateDisplay.setText(formatter.format(item.getDeadline().getTime()));


        viewHolder.listItemCheckbox.setChecked(item.isDone());
        viewHolder.listItemCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setDone(isChecked);
            //notifyDataSetChanged();
            if(isChecked){
                viewHolder.listItem.setPaintFlags(viewHolder.listItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                viewHolder.cardViewItem.setCardBackgroundColor(Color.parseColor("gray"));
                viewHolder.dateDisplay.setPaintFlags(viewHolder.listItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
            else{
                viewHolder.listItem.setPaintFlags(viewHolder.listItem.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                viewHolder.cardViewItem.setCardBackgroundColor(viewHolder.defaultColor);
                viewHolder.dateDisplay.setPaintFlags(viewHolder.listItem.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                //viewHolder.listItem.setTextColor(Color.parseColor("black"));
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
            context.startActivityForResult(intent, ChecklistEventActivity.editItemRequest);
        });
    }

    public void deleteItem(int position) {
        recentlyDeletedItem = listItemTexts.get(position);
        recentlyDeletedItemPosition = position;
        listItemTexts.remove(position);
        notifyItemRemoved(position);
    }

    public void undoDelete() {
        listItemTexts.add(recentlyDeletedItemPosition,
                recentlyDeletedItem);
        notifyItemInserted(recentlyDeletedItemPosition);
    }

    @Override
    public int getItemCount() {
        return listItemTexts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

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

    public HashMap<String, Integer> getEventsOrder() {
        //TODO implement @Piotr Kocik
        //TODO trzeba też dodać synchronizację po każdej zmianie kolejności:
        // dataProvider.setEvents(null, <odpowiednie runnable>, adapter.getEventsOrder())
        // Runnable z toastem, że się nie udało zsynchronizować jest w ChecklistEventActivity, to skopiuj sobie
        return null;
    }
}
