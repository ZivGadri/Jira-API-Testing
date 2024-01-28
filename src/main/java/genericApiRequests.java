import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

public class genericApiRequests {

    public static Response post(String body, String endPoint) {
        RestAssured.baseURI = Constants.jiraBaseUrl;
        Response response =
                given().header("Content-Type","application/json")
                        .body(body).when().post();
        return response;
    }

    public static Response get() {
        RestAssured.baseURI = Constants.jiraBaseUrl;
        Response response =
                given().header("Content-Type","application/json")
                        .body("").when().get();
        return response;
    }

    public static Response put() {
        RestAssured.baseURI = Constants.jiraBaseUrl;
        Response response =
                given().header("Content-Type","application/json")
                        .body("").when().put();
        return response;
    }

    public static Response delete() {
        RestAssured.baseURI = Constants.jiraBaseUrl;
        Response response =
                given().header("Content-Type","application/json")
                        .body("").when().delete();
        return response;
    }


}
