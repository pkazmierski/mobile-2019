package com.example.kandydatpl;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private ArrayList<ChecklistItem> listItemTexts = new ArrayList<>();
    private Activity context;

    public RecyclerViewAdapter(ArrayList<ChecklistItem> listItemTexts, Activity context) {
        this.listItemTexts = listItemTexts;
        this.context = context;
    }

    public void add(ChecklistItem newItem){
        listItemTexts.add(newItem);
        notifyDataSetChanged();
    }

    public void remove(int position){

        listItemTexts.remove(position);
        notifyDataSetChanged();
    }

    public void edit(ChecklistItem item, int index){
        listItemTexts.set(index, item);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");

        ChecklistItem item = listItemTexts.get(i);
        viewHolder.listItem.setText(item.getTitle());
        viewHolder.listItemCheckbox.setChecked(item.isDone());
        viewHolder.listItemCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            item.setDone(isChecked);
            //notifyDataSetChanged();
        });

        viewHolder.editItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddEditListItemActivity.class);
            intent.putExtra("index", viewHolder.getAdapterPosition());
            intent.putExtra("item", item);
            context.startActivityForResult(intent, TaskListActivity.editItemRequest);
        });
    }

    @Override
    public int getItemCount() {
        return listItemTexts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView listItem;
        CheckBox listItemCheckbox;
        Button editItem;
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listItem = itemView.findViewById(R.id.itemText);
            listItemCheckbox = itemView.findViewById(R.id.itemCheckBox);
            editItem = itemView.findViewById(R.id.editItemButton);
            this.view = itemView;
        }
    }
}
