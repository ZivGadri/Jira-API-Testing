import io.restassured.RestAssured;

public class CommonAPIRequests {

    public static String createSessionId() {

        RestAssured.baseURI = Constants.jiraBaseURL;
        return "";
    }
}
