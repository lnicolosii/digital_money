package com.digitalmoney.tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class AuthenticationSmokeTests extends BaseTest {

    @Test(priority = 1, description = "TC_S2_003: User Login - Valid Credentials")
    public void testValidUserLogin() {
        String loginPayload = String.format("""
                {
                    "email": "%s",
                    "password": "%s"
                }
                """, emailUser, passwordUser);


        Response response = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post(AUTH_ENDPOINT + "/login");

        response.then()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("accessToken", not(emptyString()));

        String token = response.jsonPath().getString("accessToken");
        assertNotNull(token, "JWT token should not be null");
        assertTrue(token.length() > 0, "JWT token should not be empty");
    }

    @Test(priority = 2, description = "TC_S2_004: Token Validation - Valid Token")
    public void testValidTokenValidation() {
        // First login to get a valid token
        authenticateUser();

        Response response = validateToken(jwtToken);

        response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("id", greaterThan(0));

        Long returnedUserId = response.jsonPath().getLong("id");
        assertNotNull(returnedUserId, "User ID should not be null");
        assertTrue(returnedUserId > 0, "User ID should be positive");
    }

    @Test(priority = 3, description = "TC_S2_021: User Registration - Valid Data")
    public void testValidUserRegistration() {
        String uniqueEmail = "newuser@test.com";
        String registrationPayload = String.format("""
                {
                    "email": "%s",
                    "password": "SecurePass123",
                    "firstName": "New",
                    "lastName": "User",
                    "dni": "642486322",
                    "phone": "1234567890"
                }
                """, uniqueEmail);

        Response response = given()
                .contentType("application/json")
                .body(registrationPayload)
                .when()
                .post(AUTH_ENDPOINT + "/register");

        response.then()
                .statusCode(anyOf(is(200), is(201)))
                .body("id", notNullValue())
                .body("email", equalTo(uniqueEmail))
                .body("firstName", equalTo("New"))
                .body("lastName", equalTo("User"));
    }

    @Test(priority = 4, description = "Token Validation - Invalid Token (Negative Test)")
    public void testInvalidTokenValidation() {
        String invalidToken = "invalid.jwt.token";

        Response response = validateToken(invalidToken);

        response.then()
                .statusCode(anyOf(is(500), is(400)))
                .body("message", notNullValue());
    }
}