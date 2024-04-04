package reporting.testrail.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRailCaseFields {

    private Integer id;
    private boolean is_active;
    private Integer type_id;
    private String name;
    private String system_name;
    private String label;
    private String description;
    private List<TestRailFieldConfig> configs;
    private Integer display_order;
    private Boolean include_all;
    private List<Integer> template_ids;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSystem_name() {
        return system_name;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<TestRailFieldConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<TestRailFieldConfig> configs) {
        this.configs = configs;
    }

    public Integer getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(Integer display_order) {
        this.display_order = display_order;
    }

    public Boolean getInclude_all() {
        return include_all;
    }

    public void setInclude_all(Boolean include_all) {
        this.include_all = include_all;
    }

    public List<Integer> getTemplate_ids() {
        return template_ids;
    }

    public void setTemplate_ids(List<Integer> template_ids) {
        this.template_ids = template_ids;
    }

    /**
     * @return Array of strings of SuiteNames params
     */
    private String[] getSuiteInnerItems(){
        return getConfigs().get(0).getOptions().getItems().split("\n");
    }

    /**
     * @return Suites names as {@code List<String>}
     */
    public List<String> getSuiteItems() {
        return Arrays.asList(getSuiteInnerItems());
    }

    /**
     * @return Array of strings of AutomationStatus params
     */
    private String[] getAutomationStatusInnerItems(){
        return getConfigs().get(0).getOptions().getItems().split("\n");
    }

    /**
     * @return Automation Status values as {@code List<String>}
     */
    public List<String> getAutomationStatusItems() {
        return Arrays.asList(getAutomationStatusInnerItems());
    }
}

