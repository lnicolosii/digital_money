package com.digitalmoney.tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class InfrastructureSmokeTests extends BaseTest {

    @Test(priority = 1, description = "TC_S2_025: Health Check - All Services")
    public void testServiceHealthCheck() {
        // Test Eureka Server
        Response eurekaResponse = given()
                .when()
                .get("http://localhost:8761");

        eurekaResponse.then()
                .statusCode(200);

        // Test Gateway connectivity
        Response gatewayResponse = given()
                .when()
                .get(BASE_URL);

        gatewayResponse.then()
                .statusCode(anyOf(is(200), is(404), is(401))); // Any response means gateway is up
    }

    @Test(priority = 2, description = "TC_S2_020: Gateway Routing - Card Endpoints")
    public void testGatewayRoutingForCardEndpoints() {
        // Test that gateway routes card requests properly
        Response response = given()
                .contentType("application/json")
                .when()
                .get(BASE_URL + CARDS_ENDPOINT + "/account/1");

        // Should return 401 (unauthorized) or 403 (forbidden) if routing works
        // 404 would indicate routing failure
        response.then()
                .statusCode(anyOf(is(401), is(403), is(200)));
    }

    @Test(priority = 3, description = "TC_S2_023: CORS Headers Validation")
    public void testCORSHeaders() {
        Response response = given()
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "POST")
                .header("Access-Control-Request-Headers", "Content-Type, Authorization")
                .when()
                .options(BASE_URL + AUTH_ENDPOINT + "/login");

        // Check if CORS preflight request is handled
        response.then()
                .statusCode(anyOf(is(200), is(204)))
                .header("Access-Control-Allow-Origin", notNullValue());
    }

    @Test(priority = 4, description = "Service Discovery - Eureka Registration")
    public void testEurekaServiceRegistration() {
        // Check if services are registered with Eureka
        Response response = given()
                .when()
                .get("http://localhost:8761/eureka/apps");

        response.then()
                .statusCode(200)
                .contentType(anyOf(
                    containsString("application/xml"),
                    containsString("application/json")
                ));
    }

    @Test(priority = 5, description = "Gateway Authentication Filter")
    public void testGatewayAuthenticationFilter() {
        // Test that protected endpoints require authentication
        Response response = given()
                .contentType("application/json")
                .body("{}")
                .when()
                .post(BASE_URL + CARDS_ENDPOINT);

        // Should return 401 or 403 without proper JWT token
        response.then()
                .statusCode(anyOf(is(401), is(403)));
    }
}