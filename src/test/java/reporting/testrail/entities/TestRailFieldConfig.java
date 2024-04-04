package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRailFieldConfig{
private String id;
private TestRailConfigContext context;
private TestRailConfigOptions options;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TestRailConfigContext getContext() {
        return context;
    }

    public void setContext(TestRailConfigContext context) {
        this.context = context;
    }

    public TestRailConfigOptions getOptions() {
        return options;
    }

    public void setOptions(TestRailConfigOptions options) {
        this.options = options;
    }
}
