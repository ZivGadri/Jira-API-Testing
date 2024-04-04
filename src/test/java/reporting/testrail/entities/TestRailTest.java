package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRailTest {

    private int id;
    private int status_id;
    private int assignedto_id;
    private int run_id;
    private int case_id;
    private String title;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus_id() {
        return status_id;
    }

    public void setStatus_id(int status_id) {
        this.status_id = status_id;
    }

    public int getAssignedto_id() {
        return assignedto_id;
    }

    public void setAssignedto_id(int assignedto_id) {
        this.assignedto_id = assignedto_id;
    }

    public int getRun_id() {
        return run_id;
    }

    public void setRun_id(int run_id) {
        this.run_id = run_id;
    }

    public int getCase_id() {
        return case_id;
    }

    public void setCase_id(int case_id) {
        this.case_id = case_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

