import apiManager.models.Issue;
import apiManager.models.Project;
import apiManager.models.enums.IssueTypes;

public class TestHelper {

    public static Project buildProjectObject() {
        return new Project.ProjectBuilder("Example", "Test_Project").
                setProjectTypeKey("Test").
                setProjectTemplateKey("com.atlassian.jira-core-project-templates:jira-core-project-management").
                setDescription("This is a project for demo testing").
                setLead("Ziv Gadri").
                setUrl("http://atlassian.com").
                setAssigneeType("PROJECT_LEAD").
                setAvatarId(130384).
                setIssueSecurityScheme(12345).
                setPermissionScheme(12345).
                setNotificationScheme(12345).
                setCategoryId(11111).
                build();
    }

    public static Issue buildIssueObject(Project project) {
        return new Issue(project, IssueTypes.BUG);
    }
}
