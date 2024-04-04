package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRailProject {

    private int id;
    private String name;
    private String announcement;
    private boolean is_completed;
    private String completed_on;
    private String url;
    private boolean show_announcement;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public boolean isIs_completed() {
        return is_completed;
    }

    public void setIs_completed(boolean is_completed) {
        this.is_completed = is_completed;
    }

    public String getCompleted_on() {
        return completed_on;
    }

    public void setCompleted_on(String completed_on) {
        this.completed_on = completed_on;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isShow_announcement() {
        return show_announcement;
    }

    public void setShow_announcement(boolean show_announcement) {
        this.show_announcement = show_announcement;
    }
}