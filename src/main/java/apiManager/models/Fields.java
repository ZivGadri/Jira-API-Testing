package apiManager.models;

import apiManager.models.enums.IssueTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {

    private Project project;
    private String summary;
    private String description;
    private IssueType issueType;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IssueType getIssueType() {
        return issueType;
    }

    public void setIssueType(IssueType issueType) {
        this.issueType = issueType;
    }

    public Fields(Project project, IssueTypes issueType) {
        this.project = new Project(project);
        this.summary = "Test Issue Summary";
        this.description = "Test Issue Description";
        this.issueType = new IssueType(issueType);
    }
}
