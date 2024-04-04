package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class TestRailConfigContext{
private boolean is_global;
private List<Integer> project_ids;

    public boolean isIs_global() {
        return is_global;
    }

    public void setIs_global(boolean is_global) {
        this.is_global = is_global;
    }

    public List<Integer> getProject_ids() {
        return project_ids;
    }

    public void setProject_ids(List<Integer> project_ids) {
        this.project_ids = project_ids;
    }
}
