package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * These are the fields you can add to the test plan:
 *
 * Name     	    Type        Description
 * name     	    String      The name of the test plan (required)
 * description	    String      The description of the test plan
 * milestone_id	    int         The ID of the milestone to link to the test plan
 * entries  	    array       An array of objects describing the test runs of the plan (TestRailEntry)
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRailTestPlan {

    private int id;
    private String name;
    private String description;
    private Integer milestone_id;
    private String assignedto_id;
    private Boolean is_completed;
    private Long completed_on;
    private Integer passed_count;
    private Integer blocked_count;
    private Integer untested_count;
    private Integer retest_count;
    private Integer failed_count;
    private Integer custom_status1_count;
    private Integer project_id;
    private String url;
    public List<TestRailTestPlanEntry> entries;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMilestone_id() {
        return milestone_id;
    }

    public void setMilestone_id(Integer milestone_id) {
        this.milestone_id = milestone_id;
    }

    public String getAssignedto_id() {
        return assignedto_id;
    }

    public void setAssignedto_id(String assignedto_id) {
        this.assignedto_id = assignedto_id;
    }

    public Boolean getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(Boolean is_completed) {
        this.is_completed = is_completed;
    }

    public Long getCompleted_on() {
        return completed_on;
    }

    public void setCompleted_on(Long completed_on) {
        this.completed_on = completed_on;
    }

    public Integer getPassed_count() {
        return passed_count;
    }

    public void setPassed_count(Integer passed_count) {
        this.passed_count = passed_count;
    }

    public Integer getBlocked_count() {
        return blocked_count;
    }

    public void setBlocked_count(Integer blocked_count) {
        this.blocked_count = blocked_count;
    }

    public Integer getUntested_count() {
        return untested_count;
    }

    public void setUntested_count(Integer untested_count) {
        this.untested_count = untested_count;
    }

    public Integer getRetest_count() {
        return retest_count;
    }

    public void setRetest_count(Integer retest_count) {
        this.retest_count = retest_count;
    }

    public Integer getFailed_count() {
        return failed_count;
    }

    public void setFailed_count(Integer failed_count) {
        this.failed_count = failed_count;
    }

    public Integer getCustom_status1_count() {
        return custom_status1_count;
    }

    public void setCustom_status1_count(Integer custom_status1_count) {
        this.custom_status1_count = custom_status1_count;
    }

    public Integer getProject_id() {
        return project_id;
    }

    public void setProject_id(Integer project_id) {
        this.project_id = project_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TestRailTestPlanEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<TestRailTestPlanEntry> entries) {
        this.entries = entries;
    }

    public void addEntry(TestRailTestPlanEntry entry) {
        if (this.entries == null) {
            this.entries = new ArrayList<>();
        }
        this.entries.add(entry);
    }
}
