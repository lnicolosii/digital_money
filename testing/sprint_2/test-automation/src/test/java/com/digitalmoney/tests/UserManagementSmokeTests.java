package com.digitalmoney.tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class UserManagementSmokeTests extends BaseTest {

    @Test(priority = 1, description = "TC_S2_005: Update User Profile - Valid Data")
    public void testUpdateUserProfileWithValidData() {
        System.out.println("=== Testing Update User Profile ===");
        System.out.println("Using userId: " + userId);

        String updatePayload = getUserUpdatePayload();
        System.out.println("Update payload: " + updatePayload);

        Response response = getAuthenticatedRequest()
                .body(updatePayload)
                .when()
                .patch(USERS_ENDPOINT + "/" + userId);

        System.out.println("Update response status: " + response.getStatusCode());
        System.out.println("Update response body: " + response.getBody().asString());
        System.out.println("Update response headers: " + response.getHeaders());

        // Check if we got a 401 specifically to debug authentication issues
        if (response.getStatusCode() == 401) {
            System.err.println("Authentication failed! Checking token...");
            System.err.println("JWT Token is null: " + (jwtToken == null));
            if (jwtToken != null) {
                System.err.println("JWT Token length: " + jwtToken.length());
                // Try to validate token directly
                Response tokenCheck = validateToken(jwtToken);
                System.err.println("Direct token validation status: " + tokenCheck.getStatusCode());
                System.err.println("Direct token validation response: " + tokenCheck.getBody().asString());
            }
        }

        response.then()
                .statusCode(anyOf(is(200), is(204)));

        String body = response.getBody().asString();

        if (!body.isBlank()) {
            JsonPath json = new JsonPath(body);
            assertEquals(json.getString("firstName"), "Updated");
            assertEquals(json.getString("lastName"), "User");
            assertEquals(json.getString("email"), "updated@test.com");
        }

        restoreUser();
    }

    @Test(priority = 2, description = "TC_S2_017: Update Account Alias - Valid Request")
    public void testUpdateAccountAlias() {
        String aliasPayload = """
                {
                    "alias": "new.test.alias"
                }
                """;

        Response response = getAuthenticatedRequest()
                .body(aliasPayload)
                .when()
                .patch(ACCOUNTS_ENDPOINT + "/user/" + userId);

        response.then()
                .statusCode(anyOf(is(200), is(204)));

        String body = response.getBody().asString();

        if (!body.isBlank()) {
            JsonPath json = new JsonPath(body);
            assertEquals(json.getString("alias"), "new.test.alias");

        }
    }

    @Test(priority = 3, description = "Update User - Invalid User ID (Negative Test)")
    public void testUpdateUserWithInvalidId() {
        String updatePayload = getUserDefaultPayload();

        Response response = getAuthenticatedRequest()
                .body(updatePayload)
                .when()
                .patch(USERS_ENDPOINT + "/99999");

        response.then()
                .statusCode(anyOf(is(404), is(400)))
                .body("message", notNullValue());
    }

    @Test(priority = 5, description = "Update Account - Invalid User ID (Negative Test)")
    public void testUpdateAccountWithInvalidUserId() {
        String aliasPayload = """
                {
                    "alias": "test.alias"
                }
                """;

        Response response = getAuthenticatedRequest()
                .body(aliasPayload)
                .when()
                .patch(ACCOUNTS_ENDPOINT + "/user/99999");

        response.then()
                .statusCode(anyOf(is(404), is(400)))
                .body("message", notNullValue());
    }
}