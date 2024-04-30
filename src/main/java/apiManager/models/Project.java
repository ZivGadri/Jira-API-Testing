package apiManager.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {
    private static final Logger logger = LogManager.getLogger(Project.class);
    private String key;
    private String id;
    private String name;
    private String projectTypeKey;
    private String projectTemplateKey;
    private String description;
    private String lead;
    private String assigneeType;
    private int avatarId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public void setProjectTypeKey(String projectTypeKey) {
        this.projectTypeKey = projectTypeKey;
    }

    public String getProjectTemplateKey() {
        return projectTemplateKey;
    }

    public void setProjectTemplateKey(String projectTemplateKey) {
        this.projectTemplateKey = projectTemplateKey;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getAssigneeType() {
        return assigneeType;
    }

    public void setAssigneeType(String assigneeType) {
        this.assigneeType = assigneeType;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public Project(Project project) {
        this.key = project.getKey();
    }

    private Project(ProjectBuilder builder) {
        this.key = builder.key;
        this.name = builder.name;
        this.projectTypeKey = builder.projectTypeKey;
        this.projectTemplateKey = builder.projectTemplateKey;
        this.description = builder.description;
        this.lead = builder.lead;
        this.assigneeType = builder.assigneeType;
        this.avatarId = builder.avatarId;
    }

    public static class ProjectBuilder {
        private String key;
        private String name;
        private String projectTypeKey;
        private String projectTemplateKey;
        private String description;
        private String lead;
        private String assigneeType;
        private int avatarId;

        public ProjectBuilder(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public ProjectBuilder setProjectTypeKey(String projectTypeKey) {
            this.projectTypeKey = projectTypeKey;
            return this;
        }

        public ProjectBuilder setProjectTemplateKey(String projectTemplateKey) {
            this.projectTemplateKey = projectTemplateKey;
            return this;
        }

        public ProjectBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ProjectBuilder setLead(String lead) {
            this.lead = lead;
            return this;
        }

        public ProjectBuilder setAssigneeType(String assigneeType) {
            this.assigneeType = assigneeType;
            return this;
        }

        public ProjectBuilder setAvatarId(int avatarId) {
            this.avatarId = avatarId;
            return this;
        }

        @Override
        public String toString() {
            return "ProjectBuilder{" +
                    "key=" + key +
                    "name=" + name +
                    "projectTypeKey=" + projectTypeKey +
                    "projectTemplateKey=" + projectTemplateKey +
                    "description=" + description +
                    "lead=" + lead +
                    "assigneeType=" + assigneeType +
                    "avatarId=" + avatarId +
                    "}";
        }

        public Project build() {
            logger.info(toString());
            return new Project(this);
        }
    }
}