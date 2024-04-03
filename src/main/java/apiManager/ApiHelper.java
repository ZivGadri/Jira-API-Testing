package apiManager;

import apiManager.models.Issue;
import apiManager.models.Project;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import static io.restassured.RestAssured.given;

public class ApiHelper {
    private static final Logger logger = LogManager.getLogger(ApiHelper.class);
    protected static RequestSpecification requestSpecification;
    protected static ResponseSpecification responseSpecification;
    private static JsonPath jsonPath;
    private static String sessionId;

    public static void initRequestSpecifications() {
        initRequestSpecification();
    }

    public static void createSessionId() {
        String path = Constants.PROTOCOL + Constants.JIRA_BASE_URL + EndPoints.CREATE_SESSION;
        HashMap<String, Object> createSessionIdMap = new HashMap<>();
        createSessionIdMap.put("username", Constants.JIRA_USER_NAME);
        createSessionIdMap.put("password", Constants.JIRA_PASSWORD);
        Response response = APIRequests.makePostRequestToCreateSessionID(path, createSessionIdMap);
        try {
            assert response != null;
            jsonPath = new JsonPath(response.asString());
        } catch (AssertionError ae) {
            logFailAssertion(ae);
        }
        sessionId = jsonPath.getString("session.value");
    }

    public Project createNewProject(Project project) {
        initResponseSpecification(201);
        String path = EndPoints.CREATE_PROJECT;
        Response response = APIRequests.makePostRequestToCreate(path, project);
        Assert.assertEquals(getValueFromResponse(response, "key"), project.getKey());
        return (Project) response.as(Project.class);
    }

    public Issue createNewIssue(Issue issue) {
        initResponseSpecification(201);
        String path = EndPoints.CREATE_ISSUE;
        Response response = APIRequests.makePostRequestToCreate(path, issue);
        return (Issue) response.as(Issue.class);
    }

    /**
     * This method initializes the common request specs for the api calls to be reused in all the API requests
     */

    private static void initRequestSpecification() {
        RequestSpecification req = new RequestSpecBuilder().
                setBaseUri(Constants.PROTOCOL + Constants.JIRA_BASE_URL).
                addHeader("cookie", "JSESSIONID=" + sessionId).
                setContentType(ContentType.JSON).
                build();
        requestSpecification = given().spec(req).log().ifValidationFails();
    }

    /**
     * This method initializes the common response specs for the api calls to be reused in all the API requests
     */

    private void initResponseSpecification(int expectedStatusCode) {
        responseSpecification = new ResponseSpecBuilder().
                expectContentType(ContentType.JSON).
                expectStatusCode(expectedStatusCode).
                build();
    }

    /**
     * This method logs and throws an assertion error
     * and uses {@link #getMethodName(Throwable)}
     * to also print the name of the method that called this one
     *
     * @param throwable throwable as caught
     */
    protected static void logFailAssertion(Throwable throwable) {
        logger.error("\n{} - request failed due to unexpected status code:\n{}", getMethodName(throwable), throwable.getMessage());
        throw new AssertionError(throwable.getMessage());
    }

    /**
     * This method logs and throws a runtime exception for the unexpected errors and exceptions
     * and uses {@link #getMethodName(Throwable)}
     * to also print the name of the method that called this one
     *
     * @param throwable throwable as caught
     */
    protected static void logFailUnexpected(Throwable throwable) {
        logger.error("\n{} - request failed due to exception:\n{}", getMethodName(throwable), throwable.getMessage());
        throw new RuntimeException(throwable.getMessage());
    }

    /**
     * This method finds the method name for the method that called it (through the stack trace)
     *
     * @param throwable throwable received
     * @return method name found
     */
    protected static String getMethodName(Throwable throwable) {
        AtomicReference<String> methodName = new AtomicReference<>("");
        Arrays.stream(throwable.getStackTrace()).
                filter(stackTraceElement -> stackTraceElement.getClassName().contains("API")).
                forEachOrdered(stackTraceElement -> methodName.set(stackTraceElement.getMethodName()));
        return methodName.get();
    }

    private String getValueFromResponse(Response response, String keyPath) {
        JsonPath jsonPath = new JsonPath(response.asString());
        return jsonPath.getString(keyPath);
    }


}
