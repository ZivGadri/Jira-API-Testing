import apiManager.ApiHelper;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class APITests extends TestFlows {

    public APITests(ApiHelper apiHelper) {
        super(apiHelper);
    }

    @BeforeClass
    public void beforeClass() {
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

    @AfterClass
    public void afterTest() {
        softAssert.assertAll();
    }

}
