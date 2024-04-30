import org.testng.annotations.*;
import reporting.DemoProject_Jira;
import reporting.TestListeners;


@Listeners(TestListeners.class)
public class APITests extends TestFlows {

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        super.beforeClass();
    }

    @Test(priority = 1)
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test creating a new project by API and assert using UI")
    public void testCreatingNewProject() {
        testCreateNewProjectAndAssertWithUI();
    }

    @Test(priority = 2, dependsOnMethods = "testCreatingNewProject")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test creating a new issue by API and assert using UI")
    public void testCreatingNewIssue() {
        testCreatingANewIssueAndAssertUsingUI();
    }

    @Test(priority = 3, dependsOnMethods = "testCreatingNewIssue")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test creating a new comment by API and assert using UI")
    public void testAddNewComment() {
        testAddingCommentToNewIssue();
    }

    @Test(priority = 4, dependsOnMethods = "testAddNewComment")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test updating a comment by API and assert using UI")
    public void testUpdatingComment() {
        this.testUpdatingCommentInIssue();
    }

    @Test(priority = 5, dependsOnMethods = "testAddNewComment")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test deleting a comment by API and assert using UI")
    public void testDeleteComment() {
        testDeletingACommentFromIssue();
    }

    @Test(priority = 6, dependsOnMethods = "testCreatingNewIssue")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test deleting an issue by API and assert using UI")
    public void testDeleteIssue() {
        testDeleteIssueFromProject();
    }

    @Test(priority = 7, dependsOnMethods = "testCreatingNewProject")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test deleting a project by API and assert using UI")
    public void testDeleteProject() {
        testDeleteProjectFromWorkspace();
    }
}