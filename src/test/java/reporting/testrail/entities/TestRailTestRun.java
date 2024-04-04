package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRailTestRun {

    private Integer id;
    private String name;
    private String description;
    private Integer suite_id;
    private Integer milestone_id;
    private String assignedto_id;
    private Boolean include_all;
    private Boolean  is_completed;
    private String completed_on;
    private String config;
    private Integer passed_count;
    private Integer blocked_count;
    private Integer untested_count;
    private Integer retest_count;
    private Integer failed_count;
    private Integer project_id;
    private String url;
    private Integer plan_id;

    private List<Integer> case_ids;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getSuite_id() {
        return suite_id;
    }

    public void setSuite_id(Integer suite_id) {
        this.suite_id = suite_id;
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

    public Boolean getInclude_all() {
        return include_all;
    }

    public void setInclude_all(Boolean include_all) {
        this.include_all = include_all;
    }

    public Boolean getIs_completed() {
        return is_completed;
    }

    public void setIs_completed(Boolean is_completed) {
        this.is_completed = is_completed;
    }

    public String getCompleted_on() {
        return completed_on;
    }

    public void setCompleted_on(String completed_on) {
        this.completed_on = completed_on;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
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

    public Integer getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(Integer plan_id) {
        this.plan_id = plan_id;
    }

    public List<Integer> getCase_ids() {
        return case_ids;
    }

    public void setCase_ids(List<Integer> case_ids) {
        this.case_ids = case_ids;
    }
}
