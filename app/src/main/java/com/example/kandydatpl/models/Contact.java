package com.example.kandydatpl.models;

import java.util.Objects;

import javax.annotation.Nonnull;

public class Contact {
    private String id;
    private String email;
    private int phone;

    public Contact(@Nonnull String id) {
        this.id = id;
    }

    public Contact(@Nonnull String id, @Nonnull String email, @Nonnull int phone) {
        this.id = id;
        this.email = email;
        this.phone = phone;
    }

    public String getId() {
        return id;
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
