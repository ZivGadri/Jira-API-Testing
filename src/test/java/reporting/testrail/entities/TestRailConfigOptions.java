package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRailConfigOptions{
    private boolean is_required;
    private String items;

    public boolean isIs_required() {
        return is_required;
    }

    public void setIs_required(boolean is_required) {
        this.is_required = is_required;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }
}
