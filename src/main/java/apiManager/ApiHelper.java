package apiManager;

import apiManager.models.Comment;
import apiManager.models.Issue;
import apiManager.models.Project;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.RetryPolicy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;

public class ApiHelper {
    private static final Logger logger = LogManager.getLogger(ApiHelper.class);
    public static String JIRA_BASE_URL;
    protected static RequestSpecification requestSpecification;
    protected static ResponseSpecification responseSpecification;
    protected RetryPolicy createResourcesRetryPolicy;
    protected RetryPolicy getPutResourcesRetryPolicy;
    protected RetryPolicy deleteResourcesRetryPolicy;
    protected RestAssuredConfig restAssuredConfig;
    private String sessionId;

    public ApiHelper(String jiraUsername, String jiraPassword, String jiraBaseUrl) {
        JIRA_BASE_URL = jiraBaseUrl;
        initRetryPolicies();
        createSessionId(jiraUsername, jiraPassword);
        initRequestSpecification();
    }

    private void initRetryPolicies() {
        logger.info("Initializing retry policies...");
        restAssuredConfig = RestAssured.config()
                .encoderConfig(encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false));
        createResourcesRetryPolicy = new RetryPolicy<>()
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <201> but was <404>."))
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <201> but was <500>."))
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <201> but was <502>."))
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <201> but was <504>."))
                .withDelay(Duration.ofSeconds(1))
                .withMaxRetries(3)
                .onRetriesExceeded(throwable -> logFailureOnRetriesExceeded(throwable.getFailure(), throwable.getAttemptCount(), throwable.getElapsedTime().getSeconds()));
        getPutResourcesRetryPolicy = new RetryPolicy<>()
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <200> but was <400>."))
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <200> but was <401>."))
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <200> but was <404>."))
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <200> but was <504>."))
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <200> but was <502>."))
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <200> but was <500>."))
                .withDelay(Duration.ofSeconds(1))
                .withMaxRetries(3)
                .onRetriesExceeded(throwable -> logFailureOnRetriesExceeded(throwable.getFailure(), throwable.getAttemptCount(), throwable.getElapsedTime().getSeconds()));
        deleteResourcesRetryPolicy = new RetryPolicy<>()
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <204> but was <401>."))
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <204> but was <403>."))
                .handleIf((o, throwable) -> throwable.getMessage().contains("Expected status code <204> but was <404>."))
                .withDelay(Duration.ofSeconds(1))
                .withMaxRetries(3)
                .onRetriesExceeded(throwable -> logFailureOnRetriesExceeded(throwable.getFailure(), throwable.getAttemptCount(), throwable.getElapsedTime().getSeconds()));
    }

    public void createSessionId(String jiraUsername, String jiraPassword) {
        logger.info("Extracting session ID");
        String path = JIRA_BASE_URL + EndPoints.CREATE_SESSION;
        HashMap<String, Object> createSessionIdMap = new HashMap<>();
        createSessionIdMap.put("username", jiraUsername);
        createSessionIdMap.put("password", jiraPassword);
        Response response = (Response) Failsafe.with(createResourcesRetryPolicy).get(() -> APIRequests.makePostRequestToCreateSessionID(path, createSessionIdMap));
        try {
            assert response != null;
            sessionId = getValueFromResponse(response, "session.value");
        } catch (AssertionError ae) {
            logFailAssertion(ae);
        }
    }

    public String createProjectUsingCurl(Project project) {
        logger.info("Starting API call for creating a new project");
        String uri = JIRA_BASE_URL + EndPoints.CREATE_PROJECT;
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBodyJson;
        try {
            requestBodyJson = objectMapper.writeValueAsString(project);
        } catch (JsonProcessingException jpe) {
            logger.error("Could not map the object to a json string");
            throw new RuntimeException(jpe.getMessage());
        }
        String response = APIRequests.makeCurlPostRequestForCreatingProject(uri, sessionId, requestBodyJson);
        return getValueFromResponse(response, "id");
    }

    public Issue createNewIssue(Issue issue) {
        logger.info("Starting API call for creating a new issue");
        initResponseSpecification(201);
        String path = EndPoints.CREATE_ISSUE;
        Response response = (Response) Failsafe.with(createResourcesRetryPolicy).get(() -> APIRequests.makePostRequestToCreate(path, issue));
        return (Issue) response.as(Issue.class);
    }

    public Comment addComment(Comment comment, Issue issue) {
        logger.info("Starting API call for add a new comment");
        initResponseSpecification(201);
        String path = String.format(EndPoints.ADD_COMMENT, issue.getId());
        Response response = (Response) Failsafe.with(createResourcesRetryPolicy).get(() -> APIRequests.makePostRequestToCreate(path, comment));
        return (Comment) response.as(Comment.class);
    }

    public Comment updateComment(Comment comment, Issue issue) {
        logger.info("Starting API call for updating a comment");
        initResponseSpecification(200);
        String path = String.format(EndPoints.UPDATE_COMMENT, issue.getId(), comment.getId());
        Response response = (Response) Failsafe.with(getPutResourcesRetryPolicy).get(() -> APIRequests.makePutRequestToUpdate(path, comment));
        return (Comment) response.as(Comment.class);
    }

    public void deleteComment(Comment comment, Issue issue) {
        logger.info("Starting API call for deleting a comment");
        initResponseSpecification(204);
        String path = String.format(EndPoints.DELETE_COMMENT, issue.getId(), comment.getId());
        Failsafe.with(deleteResourcesRetryPolicy).get(() -> APIRequests.makeDeleteRequest(path));
    }

    public void deleteIssue(Issue issue) {
        logger.info("Starting API call for deleting an issue");
        initResponseSpecification(204);
        String path = String.format(EndPoints.DELETE_ISSUE, issue.getId());
        Failsafe.with(deleteResourcesRetryPolicy).get(() -> APIRequests.makeDeleteRequest(path));
    }

    public void deleteProject(Project project) {
        logger.info("Starting API call for deleting a project");
        initResponseSpecification(204);
        String path = String.format(EndPoints.DELETE_PROJECT, project.getId());
        Failsafe.with(deleteResourcesRetryPolicy).get(() -> APIRequests.makeDeleteRequest(path));
    }

    public int getNumOfCommentsForIssue(Issue issue) {
        logger.info("Retrieving the number of comments for issue");
        initResponseSpecification(200);
        String path = String.format(EndPoints.GET_ALL_COMMENTS, issue.getId());
        Response response = (Response) Failsafe.with(getPutResourcesRetryPolicy).get(() -> APIRequests.makeGetRequestToRetrieve(path));
        return Integer.parseInt(getValueFromResponse(response, "total"));
    }

    public int getDeletedIssueGetResponseStatusCode(Issue issue) {
        logger.info("Starting API call getting a deleted issue");
        initResponseSpecification(404);
        String path = String.format(EndPoints.GET_ISSUE, issue.getId());
        Response response = (Response) Failsafe.with(getPutResourcesRetryPolicy).get(() -> APIRequests.makeGetRequestToRetrieve(path));
        return response.getStatusCode();
    }

    public int getDeletedProjectGetResponseStatusCode(Project project) {
        initResponseSpecification(404);
        String path = String.format(EndPoints.GET_PROJECT, project.getId());
        Response response = (Response) Failsafe.with(getPutResourcesRetryPolicy).get(() -> APIRequests.makeGetRequestToRetrieve(path));
        return response.getStatusCode();
    }

    /**
     * This method initializes the common request specs for the api calls to be reused in all the API requests
     */

    private void initRequestSpecification() {
        RequestSpecification req = new RequestSpecBuilder().
                setBaseUri(JIRA_BASE_URL).
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

    private String getValueFromResponse(Response response, String keyPath) {
        JsonPath jsonPath = new JsonPath(response.asString());
        return jsonPath.getString(keyPath);
    }

    private String getValueFromResponse(String response, String keyPath) {
        JsonPath jsonPath = new JsonPath(response);
        return jsonPath.getString(keyPath);
    }

    /**
     * This method logs and throws an assertion error
     * and uses {@link #getMethodName(Throwable)}
     * to also print the name of the method that called this one
     *
     * @param throwable throwable as caught
     */
    public static void logFailAssertion(Throwable throwable) {
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
    public static void logFailUnexpected(Throwable throwable) {
        logger.error("\n{} - request failed due to exception:\n{}", getMethodName(throwable), throwable.getMessage());
        throw new RuntimeException(throwable.getMessage());
    }

    /**
     * This method logs and throws an assertion error for exceeded retries
     * and uses {@link #getMethodName(Throwable)} to also log the name of
     * the method that called it
     *
     * @param throwable  Throwable as caught
     * @param attempts   Number of attempts made for the request
     * @param duration   The duration of the attempts done for the request
     */

    private void logFailureOnRetriesExceeded(Throwable throwable, int attempts, long duration) {
        logger.error("\n{} - request failed with {} attempts spanning {} seconds.\n{}", getMethodName(throwable), attempts, duration, throwable.getMessage());
        throw new AssertionError(throwable.getMessage());
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

    public static void threadSleepLog(long sec, String extraDetails) {
        logger.info("Thread is sleeping for {} second(s) {}", sec, extraDetails);
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }
}