import apiManager.ApiHelper;
import apiManager.models.Comment;
import apiManager.models.Issue;
import apiManager.models.Project;
import org.testng.annotations.BeforeSuite;

public class BaseTests extends TestHelper {

    protected ApiHelper apiHelper;
    protected Project testProject;
    protected Issue testIssue;
    protected Comment testComment;
    protected String testCommentText = "This is a test comment";

    @BeforeSuite
    private void beforeSuite() {
        ApiHelper.createSessionId();
        ApiHelper.initRequestSpecifications();
    }



}
