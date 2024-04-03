import apiManager.ApiHelper;
import apiManager.models.Issue;
import apiManager.models.Project;
import org.testng.annotations.BeforeSuite;

public class BaseTests extends TestHelper {

    protected Project testProject;
    protected Issue testIssue;

    @BeforeSuite
    private void beforeSuite() {
        ApiHelper.createSessionId();
        ApiHelper.initRequestSpecifications();
    }



}
