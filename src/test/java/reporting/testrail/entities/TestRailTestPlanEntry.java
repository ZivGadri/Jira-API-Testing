package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * These are the fields you can add to the test plan Entry:
 *
 * Name	            Type	Description
 * suite_id         int     The ID of the test suite for the test run(s) (required)
 * name             string	The name of the test run(s)
 * description      string	The description of the test run(s) (requires TestRail 5.2 or later)
 * assignedto_id	int     The ID of the user the test run(s) should be assigned to
 * include_all      bool	True for including all test cases of the test suite and false for a custom case selection (default: true)
 * case_ids         array	An array of case IDs for the custom case selection
 * config_ids       array	An array of configuration IDs used for the test runs of the test plan entry (requires TestRail 3.1 or later)
 * runs             array	An array of test runs with configurations, please see the example below for details (requires TestRail 3.1 or later)
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRailTestPlanEntry {

    private String id;
    private Integer suite_id;
    private String name;
    public List<Integer> case_ids;
    public List<TestRailTestRun> runs;
    public Boolean include_all;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSuite_id() {
        return suite_id;
    }

    public void setSuite_id(Integer suite_id) {
        this.suite_id = suite_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getCase_ids() {
        return case_ids;
    }

    public void setCase_ids(List<Integer> case_ids) {
        this.case_ids = case_ids;
    }

    public List<TestRailTestRun> getRuns() {
        return runs;
    }

    public void setRuns(List<TestRailTestRun> runs) {
        this.runs = runs;
    }

    public Boolean getInclude_all() {
        return include_all;
    }

    public void setInclude_all(Boolean include_all) {
        this.include_all = include_all;
    }
}
