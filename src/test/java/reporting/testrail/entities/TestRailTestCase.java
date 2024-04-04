package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Strings;

import java.util.HashMap;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRailTestCase {

    private Integer id;
    private String title;
    private Integer section_id;
    private Integer type_id;
    private Integer priority_id;
    private String refs;
    private Integer milestone_id;
    private Integer suite_id;
    private Integer template_id;

    private Boolean custom_is_automated;
    private String custom_automation_status;
    private String custom_custom_case_endpoint;
    private String custom_test_case_author;
    private List<Integer> custom_suiteslist;
    private String custom_steps;
    private String custom_expected;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSection_id() {
        return section_id;
    }

    public void setSection_id(Integer section_id) {
        this.section_id = section_id;
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public Integer getPriority_id() {
        return priority_id;
    }

    public void setPriority_id(Integer priority_id) {
        this.priority_id = priority_id;
    }

    public String getRefs() {
        return refs;
    }

    public void setRefs(String refs) {
        this.refs = refs;
    }

    public boolean hasRefs(String refs) {
        return Strings.nullToEmpty(this.refs).equalsIgnoreCase(Strings.nullToEmpty(refs));
    }

    public Integer getMilestone_id() {
        return milestone_id;
    }

    public void setMilestone_id(Integer milestone_id) {
        this.milestone_id = milestone_id;
    }

    public Integer getSuite_id() {
        return suite_id;
    }

    public void setSuite_id(Integer suite_id) {
        this.suite_id = suite_id;
    }

    public Integer getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(Integer template_id) {
        this.template_id = template_id;
    }

    public Boolean getCustom_is_automated() {
        return custom_is_automated;
    }

    public void setCustom_is_automated(Boolean custom_is_automated) {
        this.custom_is_automated = custom_is_automated;
    }

    public String getCustom_automation_status() {
        return custom_automation_status;
    }

    public void setCustom_automation_status(String custom_automation_status) {
        this.custom_automation_status = custom_automation_status;
    }

    public String getCustom_custom_case_endpoint() {
        return custom_custom_case_endpoint;
    }

    public void setCustom_custom_case_endpoint(String custom_custom_case_endpoint) {
        this.custom_custom_case_endpoint = custom_custom_case_endpoint;
    }

    public boolean hasEndpoint(String endpoint) {
        return Strings.nullToEmpty(this.custom_custom_case_endpoint).equals(Strings.nullToEmpty(endpoint));
    }

    public String getCustom_test_case_author() {
        return custom_test_case_author;
    }

    public void setCustom_test_case_author(String custom_test_case_author) {
        this.custom_test_case_author = custom_test_case_author;
    }

    public List<Integer> getCustom_suiteslist() {
        return custom_suiteslist;
    }

    public void setCustom_suiteslist(List<Integer> custom_suiteslist) {
        this.custom_suiteslist = custom_suiteslist;
    }

    public String getCustom_expected() {
        return custom_expected;
    }

    public void setCustom_expected(String custom_expected) {
        this.custom_expected = custom_expected;
    }

    private List<HashMap<String, String>> custom_steps_separated;

    public String getCustom_steps() {
        return custom_steps;
    }

    public void setCustom_steps(String custom_steps) {
        this.custom_steps = custom_steps;
    }

    public List<HashMap<String, String>> getCustom_steps_separated() {
        return custom_steps_separated;
    }

    public void setCustom_steps_separated(List<HashMap<String, String>> custom_steps_separated) {
        this.custom_steps_separated = custom_steps_separated;
    }
}
