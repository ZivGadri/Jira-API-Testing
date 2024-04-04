package apiManager;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class APIRequests extends ApiHelper {

    protected static Response makePostRequestToCreateSessionID(String path, Object credentials) {
        try {
            return given().
                    contentType("application/json").
                    body(credentials).
                    when().
                    log().ifValidationFails().
                    post(path).
                    then().
                    log().ifValidationFails().
                    statusCode(200).
                    extract().response();
        } catch (AssertionError ae) {
            logFailAssertion(ae);
        } catch (Exception e) {
            logFailUnexpected(e);
        }
        return null;
    }

    /**
     * This method makes an API POST request to a given path with a given access token and body
     *
     * @param path     path for the request
     * @param toCreate content to create (request body)
     * @return response received
     */
    protected static Response makePostRequestToCreate(String path, Object toCreate) {
        try {
            return
            requestSpecification.
                    body(toCreate).
                    when().
                    post(path).
                    then().
                    spec(responseSpecification).
                    log().ifValidationFails().
                    extract().
                    response();
        } catch (AssertionError ae) {
            logFailAssertion(ae);
        } catch (Exception e) {
            logFailUnexpected(e);
        }
        return null;
    }

    /**
     * This method makes an API GET request to a given path with a given access token
     *
     * @param path  path for the request
     * @return response received
     */
    protected static Response makeGetRequestToRetrieve(String path) {
        try {
            return
            requestSpecification.
                    when().
                    get(path).
                    then().
                    spec(responseSpecification).
                    log().ifValidationFails().
                    extract().
                    response();
        } catch (AssertionError ae) {
            logFailAssertion(ae);
        } catch (Exception e) {
            logFailUnexpected(e);
        }
        return null;
    }

    /**
     * This method makes an API PUT request to a given path
     * with a given access token, content type and body
     *
     * @param toUpdate    content to update (request body)
     * @param path        path for the request
     * @return response received
     */
    protected static Response makePutRequestToUpdate(String path, Object toUpdate) {
        try {
            return
            requestSpecification.
                    body(toUpdate).
                    when().
                    put(path).
                    then().
                    spec(responseSpecification).
                    log().ifValidationFails().
                    extract().
                    response();
        } catch (AssertionError ae) {
            logFailAssertion(ae);
        } catch (Exception e) {
            logFailUnexpected(e);
        }
        return null;
    }

    /**
     * This method makes an API DELETE request to a given path
     *
     * @param path path for the request
     */
    protected static Response makeDeleteRequest(String path) {
        try {
            return
            requestSpecification.
                    when().
                    delete(path).
                    then().
                    spec(responseSpecification).
                    log().ifValidationFails().
                    extract().
                    response();
        } catch (AssertionError ae) {
            logFailAssertion(ae);
        } catch (Exception e) {
            logFailUnexpected(e);
        }
        return null;
    }
}
