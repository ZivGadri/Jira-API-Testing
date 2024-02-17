package apiManager;

import io.restassured.response.Response;

public class CommonAPIRequests extends ApiHelper {

    public static Response createSessionId() {
        return GenericApiRequests.post("{ 'username': " + Constants.jiraUserName + ", 'password': " + Constants.jiraPassword + " }", EndPoints.createSession);
    }

    public static Response createProject() {
        return GenericApiRequests.post(RequestBodyTemplates.CREATE_PROJECT.getBodyTemplate(),
                EndPoints.createProject,
                "Cookie", "JSESSIONID=" + sessionId,
                "Content-Type","application/json");
    }
}
