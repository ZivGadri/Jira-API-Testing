import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;
import reporting.TestListeners;


@Listeners(TestListeners.class)
public class APITests extends TestFlows {

    @BeforeClass
    public void beforeClass() {
        super.beforeClass();
    }

    @BeforeTest
    public void beforeTest() {
        softAssert = new SoftAssert();
    }

    @Test
    public void testCreatingNewProject() {
        testCreateNewProjectAndAssertWithUI();
    }

    @Test
    public void testCreatingNewIssue() {
        testCreatingANewIssueAndAssertUsingUI();
    }

    @Test
    public void testAddNewComment() {
        testAddingACommentToNewIssue();
    }

    @Test
    public void testUpdatingComment() {
        testUpdatingAComment();
    }

    @Test
    public void testDeleteComment() {
        testDeletingACommentFromIssue();
    }

    @Test
    public void testDeleteIssue() {
        testDeleteIssueFromProject();
    }

    @Test
    public void testDeleteProject() {
        testDeleteProjectFromWorkspace();
    }

    @AfterTest
    public void afterTest() {
        softAssert.assertAll();
    }

}
