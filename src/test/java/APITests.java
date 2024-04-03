import apiManager.ApiHelper;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class APITests extends TestFlows {

    @BeforeClass
    public void beforeClass() {
        apiHelper = new ApiHelper();
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
    }

    @Test
    public void testDeleteIssue() {

    }

    @Test
    public void testDeleteProject() {

    }

    @AfterClass
    public void afterTest() {
        softAssert.assertAll();
    }

}
