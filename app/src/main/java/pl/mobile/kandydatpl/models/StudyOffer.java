package pl.mobile.kandydatpl.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StudyOffer {
    private String id;
    private List<String> tags;
    private String content;
    private String title;

    public StudyOffer(String id, List<String> tags, String content, String title) {
        this.id = id;
        this.tags = tags;
        this.content = content;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyOffer that = (StudyOffer) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "StudyOffer{" +
                "id='" + id + '\'' +
                ", tags=" + tags +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    //TODO remove later
    public StudyOffer() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
