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
    private String url;
    private String assigneeType;
    private int avatarId;
    private int issueSecurityScheme;
    private int permissionScheme;
    private int notificationScheme;
    private int categoryId;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public int getIssueSecurityScheme() {
        return issueSecurityScheme;
    }

    public void setIssueSecurityScheme(int issueSecurityScheme) {
        this.issueSecurityScheme = issueSecurityScheme;
    }

    public int getPermissionScheme() {
        return permissionScheme;
    }

    public void setPermissionScheme(int permissionScheme) {
        this.permissionScheme = permissionScheme;
    }

    public int getNotificationScheme() {
        return notificationScheme;
    }

    public void setNotificationScheme(int notificationScheme) {
        this.notificationScheme = notificationScheme;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
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
        this.url = builder.url;
        this.assigneeType = builder.assigneeType;
        this.avatarId = builder.avatarId;
        this.issueSecurityScheme = builder.issueSecurityScheme;
        this.permissionScheme = builder.permissionScheme;
        this.notificationScheme = builder.notificationScheme;
        this.categoryId = builder.categoryId;
    }

    public static class ProjectBuilder {
        private String key;
        private String name;
        private String projectTypeKey;
        private String projectTemplateKey;
        private String description;
        private String lead;
        private String url;
        private String assigneeType;
        private int avatarId;
        private int issueSecurityScheme;
        private int permissionScheme;
        private int notificationScheme;
        private int categoryId;

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

        public ProjectBuilder setUrl(String url) {
            this.url = url;
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

        public ProjectBuilder setIssueSecurityScheme(int issueSecurityScheme) {
            this.issueSecurityScheme = issueSecurityScheme;
            return this;
        }

        public ProjectBuilder setPermissionScheme(int permissionScheme) {
            this.permissionScheme = permissionScheme;
            return this;
        }

        public ProjectBuilder setNotificationScheme(int notificationScheme) {
            this.notificationScheme = notificationScheme;
            return this;
        }

        public ProjectBuilder setCategoryId(int categoryId) {
            this.categoryId = categoryId;
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
                    "url=" +url +
                    "assigneeType=" + assigneeType +
                    "avatarId=" + avatarId +
                    "issueSecurityScheme=" + issueSecurityScheme +
                    "permissionScheme=" + permissionScheme +
                    "notificationScheme=" + notificationScheme +
                    "categoryId=" + categoryId +
                    "}";
        }

        public Project build() {
            logger.info(toString());
            return new Project(this);
        }
    }
}