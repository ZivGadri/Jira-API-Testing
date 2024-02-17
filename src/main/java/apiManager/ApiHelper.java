package apiManager;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class ApiHelper {
    private static JsonPath jsonPath;
    public static String sessionId;

    public static String getSessionId() {
        Response response = CommonAPIRequests.createSessionId();
        jsonPath = new JsonPath(response.asString());
        sessionId = jsonPath.getString("session.value");
        return sessionId;
    }



}
