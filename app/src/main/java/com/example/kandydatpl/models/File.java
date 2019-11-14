package com.example.kandydatpl.models;

import java.util.Objects;

import javax.annotation.Nonnull;

public class File {
    private String id;
    private String filename;

    public File(@Nonnull String id, @Nonnull String filename) {
        this.id = id;
        this.filename = filename;
    }

    public String getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        File file = (File) o;
        return id.equals(file.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "File{" +
                "id='" + id + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}
