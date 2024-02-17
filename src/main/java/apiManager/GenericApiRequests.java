package apiManager;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;

public class GenericApiRequests {

    public static Response post(String body, String endPoint) {
        RestAssured.baseURI = Constants.jiraBaseURL;
        return (Response) given().log().all()
                .header("Content-Type","application/json")
                .body(body)
                .when().post(endPoint)
                .then().assertThat().log().ifValidationFails().statusCode(200);
    }

    public static Response post(String body, String endPoint, String var1, Object var2, Object... var3) {
        RestAssured.baseURI = Constants.jiraBaseURL;
        return (Response) given().log().all()
                .header(var1, var2, var3)
                .body(body)
                .when().post(endPoint)
                .then().assertThat().log().ifValidationFails().statusCode(201);
    }

    public static Response get() {
        RestAssured.baseURI = Constants.jiraBaseURL;
        Response response =
                given().header("Content-Type","application/json")
                        .body("").when().get();
        return response;
    }

    public static Response put() {
        RestAssured.baseURI = Constants.jiraBaseURL;
        Response response =
                given().header("Content-Type","application/json")
                        .body("").when().put();
        return response;
    }

    public static Response delete() {
        RestAssured.baseURI = Constants.jiraBaseURL;
        Response response =
                given().header("Content-Type","application/json")
                        .body("").when().delete();
        return response;
    }


}
