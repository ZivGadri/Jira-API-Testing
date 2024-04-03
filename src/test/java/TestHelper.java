import apiManager.models.Comment;
import apiManager.models.Issue;
import apiManager.models.Project;
import apiManager.models.enums.IssueTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestHelper {
    private static final Logger logger = LogManager.getLogger(TestHelper.class);

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

    public static Comment createCommentObject(String comment) {
        return new Comment(comment);
    }

    public static void threadSleepLog(long sec) {
        logger.info("Thread is sleeping for {} second(s)", sec);
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}
