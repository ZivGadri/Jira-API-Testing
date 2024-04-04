import apiManager.ApiHelper;
import apiManager.models.Comment;
import apiManager.models.Issue;
import apiManager.models.Project;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

public class BaseTests extends TestHelper {

    protected ApiHelper apiHelper;
    protected Project testProject;
    protected Issue testIssue;
    protected Comment testComment;
    protected String testCommentText = "This is a test comment";
    protected String updatedTestCommentText = "This is an updated test comment";
    protected SoftAssert softAssert;

    public BaseTests(ApiHelper apiHelper) {
        super(apiHelper);
    }

    @BeforeSuite
    private void beforeSuite() {

    }



}
