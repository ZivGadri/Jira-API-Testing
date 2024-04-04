package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRailTestResults {

    private List<TestRailTestResult> results;

    public List<TestRailTestResult> getResults() {
        return results;
    }

    public void addResult(TestRailTestResult result) {
        if (results == null) {
            results = new ArrayList<>();
        }
        results.add(result);
    }

    public void setResults(List<TestRailTestResult> results) {
        this.results = results;
    }
}
