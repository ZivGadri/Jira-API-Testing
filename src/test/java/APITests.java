import apiManager.ApiHelper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class APITests extends TestFlows {

    @BeforeClass
    public void beforeClass() {
        apiHelper = new ApiHelper();
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

}
