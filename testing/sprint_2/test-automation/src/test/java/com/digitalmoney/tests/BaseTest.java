package com.digitalmoney.tests;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertEquals;

public class BaseTest {

    protected static final String BASE_URL = "http://localhost:3500";
    protected static final String AUTH_ENDPOINT = "/auth";
    protected static final String USERS_ENDPOINT = "/users";
    protected static final String ACCOUNTS_ENDPOINT = "/accounts";
    protected static final String CARDS_ENDPOINT = "/cards";

    protected String jwtToken;
    protected Long userId = 1L;
    protected Long accountId = 1L;
    protected String emailUser = "user@test.com";
    protected String passwordUser = "password123";

    @BeforeClass
    public void setupClass() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeMethod
    public void setup() {
        authenticateUser();
    }

    protected void authenticateUser() {
        // Intentar con el email original primero
        Response response = tryLogin(emailUser, passwordUser);

        // Si falla, intentar con el email actualizado (en caso de que el test haya cambiado el email)
        if (response.getStatusCode() != 200) {
            System.out.println("Login failed with original email, trying with updated email...");
            response = tryLogin("updated@test.com", passwordUser);
        }

        if (response.getStatusCode() == 200) {
            jwtToken = response.jsonPath().getString("accessToken");
            if (jwtToken != null) {
                Response tokenValidation = validateToken(jwtToken);
                if (tokenValidation.getStatusCode() == 200) {
                    userId = tokenValidation.jsonPath().getLong("id");
                    System.out.println("User ID extracted: " + userId);
                }
            }
        } else {
            System.err.println("Login failed with both emails. Status: " + response.getStatusCode());
            System.err.println("Login error response: " + response.getBody().asString());
        }
    }

    private Response tryLogin(String email, String password) {
        String loginPayload = String.format("""
                {
                    "email": "%s",
                    "password": "%s"
                }
                """, email, password);

        Response response = given()
                .contentType("application/json")
                .body(loginPayload)
                .when()
                .post(AUTH_ENDPOINT + "/login");

        System.out.println("Trying login with email: " + email + " - Status: " + response.getStatusCode());
        return response;
    }

    protected RequestSpecification getAuthenticatedRequest() {
        System.out.println("Creating authenticated request with token: " +
                (jwtToken != null ? jwtToken.substring(0, Math.min(10, jwtToken.length())) + "..." : "null"));

        return given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + jwtToken);
    }

    protected Response validateToken(String token) {
        String payload = String.format("""
                {
                    "token": "%s"
                }
                """, token);

        return given()
                .contentType("application/json")
                .body(payload)
                .when()
                .post(AUTH_ENDPOINT + "/validate-token");
    }

    protected String generateUniqueCardNumber() {
        return "4" + System.currentTimeMillis() % 1000000000000000L;
    }

    protected String getCardCreatePayload(String cardNumber, Long accountId) {
        return String.format("""
                {
                    "number": "%s",
                    "holder": "Test User",
                    "bank": "Test Bank",
                    "expirationDate": "12/25",
                    "cvv": "123",
                    "cardType": "credit",
                    "accountId": %d
                }
                """, cardNumber, accountId);
    }

    protected String getUserUpdatePayload() {
        return """
                {
                    "firstName": "Updated",
                    "lastName": "User",
                    "email": "updated@test.com",
                    "dni": "87654321",
                    "phone": "0987654321"
                }
                """;
    }

    protected String getUserDefaultPayload() {
        return """
                {
                    "firstName": "Test",
                    "lastName": "User",
                    "email": "user@test.com",
                    "dni": "87654321",
                    "phone": "0987654321"
                }
                """;
    }

    protected void restoreUser() {
        // Intentar restaurar con el token actual
        Response response = getAuthenticatedRequest()
                .body(getUserDefaultPayload())
                .when()
                .patch(USERS_ENDPOINT + "/" + userId);

        // Si falla por autenticación, obtener nuevo token y reintentar
        if (response.getStatusCode() == 401) {
            authenticateUser();
            getAuthenticatedRequest()
                    .body(getUserDefaultPayload())
                    .when()
                    .patch(USERS_ENDPOINT + "/" + userId);
        }
    }
}