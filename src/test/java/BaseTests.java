import apiManager.ApiHelper;
import org.testng.annotations.BeforeSuite;

public class BaseTests extends TestHelper {

    @BeforeSuite
    private void beforeSuite() {
        ApiHelper.createSessionId();
        ApiHelper.initRequestSpecifications();
    }



}
