package com.digitalmoney.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected static final String BASE_URL = System.getProperty("base.url", "http://localhost:3500");
    protected static final String AUTH_ENDPOINT = "/auth";
    protected static final String ACCOUNTS_ENDPOINT = "/accounts";

    protected String jwtToken;
    protected Long userId = 1L;
    protected Long testAccountId = 1L;
    protected Long testAccountId2 = 2L;
    protected String emailUser = "user@test.com";
    protected String passwordUser = "password123";

    @BeforeClass
    public void setupClass() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @BeforeMethod
    public void setupMethod() {
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

    protected String getAuthHeader() {
        return "Bearer " + jwtToken;
    }

    protected Map<String, Object> createDepositData(Double amount) {
        Map<String, Object> depositData = new HashMap<>();
        depositData.put("amount", amount);
        depositData.put("cardId", 1L); // ID de tarjeta de prueba
        return depositData;
    }
    
    protected Map<String, Object> createDepositData(Double amount, Long cardId) {
        Map<String, Object> depositData = new HashMap<>();
        depositData.put("amount", amount);
        depositData.put("cardId", cardId);
        return depositData;
    }

    // Método para crear datos de tarjeta
    protected Map<String, Object> createCardData(Long accountId) {
        Map<String, Object> cardData = new HashMap<>();
        cardData.put("accountId", accountId);
        cardData.put("holder", "Test User");
        cardData.put("bank", "Test Bank");
        cardData.put("expirationDate", "12/2025");
        cardData.put("number", 4111111111111111L);
        cardData.put("cvv", 123);
        cardData.put("cardType", "debit");
        return cardData;
    }

    // Método auxiliar para crear una tarjeta de prueba
    protected Long createTestCard(Long accountId) {
        Map<String, Object> cardData = createCardData(accountId);
        
        Response response = getAuthenticatedRequest()
                .body(cardData)
            .when()
                .post("/cards");
        
        if (response.getStatusCode() == 201) {
            return response.jsonPath().getLong("id");
        }
        return 1L; // ID por defecto si falla
    }

    // Método auxiliar para crear cuentas si es necesario
    protected Long createTestAccount() {
        Response response = getAuthenticatedRequest()
                .when()
                .post(ACCOUNTS_ENDPOINT);

        if (response.getStatusCode() == 201) {
            return response.jsonPath().getLong("id");
        }
        return null;
    }

    // Método auxiliar para obtener saldo de cuenta
    protected Double getAccountBalance(Long accountId) {
        Response response = getAuthenticatedRequest()
                .when()
                .get(ACCOUNTS_ENDPOINT + "/" + accountId);

        if (response.getStatusCode() == 200) {
            return response.jsonPath().getDouble("balance");
        }
        return null;
    }
}