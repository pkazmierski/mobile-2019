package com.example.kandydatpl.utils;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.allyants.chipview.ChipAdapter;
import com.example.kandydatpl.R;

import java.util.ArrayList;


public class CustomChipAdapter extends ChipAdapter {

    ArrayList<Object> search_data = new ArrayList<>();
    ArrayList<Object> chips = new ArrayList<>();

    public CustomChipAdapter(ArrayList<Object> search_data) {
        this.search_data = search_data;
        this.data = search_data;
    }

    @Override
    public Object getItem(int pos) {
        return search_data.get(pos);
    }

    @Override
    public boolean isSelected(int pos) {
        if (chips.contains(search_data.get(pos))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public View createSearchView(Context context, boolean is_checked, final int pos) {
        View view = View.inflate(context, R.layout.search, null);
        CheckBox cbCheck = view.findViewById(R.id.cbCheck);
        cbCheck.setText((String) search_data.get(pos));
        cbCheck.setChecked(is_checked);
        cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    chips.add(search_data.get(pos));
                    refresh();
                } else {
                    chips.remove(search_data.get(pos));
                    refresh();
                }
            }
        });
        return view;
    }

    @Override
    public View createChip(Context context, final int pos) {
        View view = View.inflate(context, R.layout.custom_chip, null);
        TextView tvChip = view.findViewById(R.id.tvChipCustom);
        tvChip.setText((String) search_data.get(pos));
        ImageView ivClose = view.findViewById(R.id.ivCloseCustom);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chips.remove(search_data.get(pos));
                refresh();
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return search_data.size();
    }

}

