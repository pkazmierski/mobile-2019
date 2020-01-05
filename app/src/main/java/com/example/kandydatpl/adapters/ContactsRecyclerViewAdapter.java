package com.example.kandydatpl.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.kandydatpl.R;
import com.example.kandydatpl.models.Contact;

import java.util.ArrayList;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "ContactsRVAdapter";

    private Context ctx;
    private ArrayList<Contact> contacts;

    public ContactsRecyclerViewAdapter(Context ctx, ArrayList<Contact> contacts) {
        this.ctx = ctx;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    //responsible for inflating the view
    //can be always the same (beside the layout name)
    public ContactsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_contact_item, parent, false);
        ContactsRecyclerViewAdapter.ViewHolder holder = new ContactsRecyclerViewAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    //change what layout look like
    public void onBindViewHolder(@NonNull ContactsRecyclerViewAdapter.ViewHolder holder, final int position) {
        holder.contactNameTxt.setText(contacts.get(position).getName());
        //TODO Recycling text phone/email fields on contact card when one is absent
        if (contacts.get(position).getPhone() != 0) {
            holder.contactPhoneTxt.setText(Integer.toString(contacts.get(position).getPhone()));
            holder.contactPhoneTxt.setOnClickListener(v -> Contact.openDialer(v.getContext(),
                    Integer.toString(contacts.get(position).getPhone())));
        }
        if (contacts.get(position).getEmail() != null) {
            holder.contactEmailTxt.setText(contacts.get(position).getEmail());
            holder.contactEmailTxt.setOnClickListener(v -> Contact.openEmailClient(v.getContext(),
                    contacts.get(position).getEmail()));
        }
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //holds individual entries in memory
        TextView contactNameTxt;
        TextView contactPhoneTxt;
        TextView contactEmailTxt;
        CardView contactCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactNameTxt = itemView.findViewById(R.id.contactNameTxt);
            contactPhoneTxt = itemView.findViewById(R.id.contactPhoneTxt);
            contactEmailTxt = itemView.findViewById(R.id.contactEmailTxt);
            contactCard = itemView.findViewById(R.id.contactCard);
        }
    }
}
