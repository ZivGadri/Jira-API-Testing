package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * These are the fields you can add to the results:
 *
 * status_id	    int         The ID of the test status
 * 1	Passed
 * 2	Blocked
 * 3	Untested (not allowed when adding a result)
 * 4	Retest
 * 5	Failed
 *
 * comment          string	    The comment / description for the test result
 * version          string	    The version or build you tested against
 * elapsed          timespan    The time it took to execute the test, e.g. "30s" or "1m 45s"
 * defects          string      A comma-separated list of defects to link to the test result
 * assignedto_id	int         The ID of a user the test should be assigned to
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRailTestResult {

    private Integer id;
    private Integer status_id;
    private Integer assignedto_id;
    private String comment;
    private String version;
    private String title;
    private String defects;
    private Integer case_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public Integer getAssignedto_id() {
        return assignedto_id;
    }

    public void setAssignedto_id(Integer assignedto_id) {
        this.assignedto_id = assignedto_id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void addComments(String comments) {
        if (this.comment == null) {
            this.comment = "";
        }
        this.comment += "\n" + comments;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDefects() {
        return defects;
    }

    public void setDefects(String defects) {
        this.defects = defects;
    }

    public Integer getCase_id() {
        return case_id;
    }

    public void setCase_id(Integer case_id) {
        this.case_id = case_id;
    }
}

