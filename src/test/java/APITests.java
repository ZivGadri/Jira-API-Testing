import apiManager.ApiHelper;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class APITests extends BaseTests {

    private ApiHelper apiHelper;
    @BeforeClass
    public void beforeClass() {
        apiHelper = new ApiHelper();
    }

    @Test
    public void testCreatingNewProject() {
        testProject = apiHelper.createNewProject(buildProjectObject());
    }

    @Test
    public void testCreatingNewIssue() {
        testIssue = apiHelper.createNewIssue(buildIssueObject(testProject));
    }

    @Test
    public void testCreatingNewComment() {

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
