package com.example.kandydatpl.models;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.kandydatpl.adapters.ContactsRecyclerViewAdapter;

import java.util.Objects;

import javax.annotation.Nonnull;

public class Contact {
    private String id;
    private String name;
    private String email;
    private int phone;

    public Contact(@Nonnull String id, @Nonnull String name) {
        this.id = id;
        this.name = name;
    }

    public Contact(String id, String name, String email, int phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public static void openDialer(Context context, String number){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    public static void openEmailClient(Context context, String email){

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id.equals(contact.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", phone=" + phone +
                '}';
    }
}
