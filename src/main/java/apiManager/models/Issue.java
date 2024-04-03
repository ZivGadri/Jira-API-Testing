package apiManager.models;

import apiManager.models.enums.IssueTypes;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {
    private Fields fields;
    private String id;
    private String key;
    private String self;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }


    public void setFields(Fields fields) {
        this.fields = fields;
    }

    public Fields getFields() {
        return fields;
    }

    public Issue(Project project, IssueTypes issueType) {
        this.fields = new Fields(project, issueType);
    }
}
