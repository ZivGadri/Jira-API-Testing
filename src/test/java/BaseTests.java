import org.testng.annotations.BeforeSuite;

public class BaseTests extends TestHelper {

    private static String sessionId;

    @BeforeSuite
    private void beforeSuite() {
        sessionId = CommonAPIRequests.createSessionId();
    }

}
