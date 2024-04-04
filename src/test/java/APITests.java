import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import reporting.DemoProject_Jira;
import reporting.TestListeners;


@Listeners(TestListeners.class)
public class APITests extends TestFlows {

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        super.beforeClass();
    }

    @BeforeTest
    public void beforeTest() {
        softAssert = new SoftAssert();
    }

    @Test
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test creating a new project by API and assert using UI")
    public void testCreatingNewProject() {
        testCreateNewProjectAndAssertWithUI();
    }

    @Test(dependsOnMethods = "testCreatingNewProject")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test creating a new issue by API and assert using UI")
    public void testCreatingNewIssue() {
        testCreatingANewIssueAndAssertUsingUI();
    }

    @Test(dependsOnMethods = "testCreatingNewIssue")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test creating a new comment by API and assert using UI")
    public void testAddNewComment() {
        testAddingACommentToNewIssue();
    }

    @Test(dependsOnMethods = "testAddNewComment")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test updating a comment by API and assert using UI")
    public void testUpdatingComment() {
        testUpdatingAComment();
    }

    @Test(dependsOnMethods = "testAddNewComment")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test deleting a comment by API and assert using UI")
    public void testDeleteComment() {
        testDeletingACommentFromIssue();
    }

    @Test(dependsOnMethods = "testCreatingNewIssue")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test deleting an issue by API and assert using UI")
    public void testDeleteIssue() {
        testDeleteIssueFromProject();
    }

    @Test(dependsOnMethods = "testCreatingNewProject")
    @DemoProject_Jira(testRailCaseId = "***", testName = "Test deleting a project by API and assert using UI")
    public void testDeleteProject() {
        testDeleteProjectFromWorkspace();
    }

    @AfterTest
    public void afterTest() {
        softAssert.assertAll();
    }

}
