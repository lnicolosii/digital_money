package com.digitalmoney.tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.*;

public class CardManagementSmokeTests extends BaseTest {

    private String createdCardId;

    @Test(priority = 1, description = "TC_S2_001: Create Card - Valid Data")
    public void testCreateCardWithValidData() {
        String uniqueCardNumber = generateUniqueCardNumber();
        String cardPayload = getCardCreatePayload(uniqueCardNumber, accountId);

        Response response = getAuthenticatedRequest()
                .body(cardPayload)
                .when()
                .post(CARDS_ENDPOINT);

        response.then()
                .statusCode(anyOf(is(200), is(201)));


        String body = response.getBody().asString();

        if (!body.isBlank()) {
            JsonPath json = new JsonPath(body);
            createdCardId = json.getString("id");
            assertEquals(json.getString("number"), uniqueCardNumber);
            assertEquals(json.getString("holder"), "Test User");
            assertEquals(json.getString("bank"), "Test Bank");
            assertEquals(json.getString("card_type"), "credit");
        }
    }

    @Test(priority = 2, description = "TC_S2_002: Get Cards by Account ID")
    public void testGetCardsByAccountId() {
        Response response = getAuthenticatedRequest()
                .when()
                .get(CARDS_ENDPOINT + "/account/" + accountId);

        response.then()
                .statusCode(200)
                .body("$", instanceOf(java.util.List.class))
                .body("size()", greaterThanOrEqualTo(0));

        if (response.jsonPath().getList("$").size() > 0) {
            response.then()
                    .body("[0].id", notNullValue())
                    .body("[0].number", notNullValue())
                    .body("[0].holder", notNullValue());
        }


    }

    @Test(priority = 3, description = "TC_S2_012: Get Specific Card - Valid Request", dependsOnMethods = "testCreateCardWithValidData")
    public void testGetSpecificCard() {
        if (createdCardId == null) {
            fail("No card created in previous test");
        }

        Response response = getAuthenticatedRequest()
                .when()
                .get(CARDS_ENDPOINT + "/" + createdCardId + "/account/" + accountId);

        response.then()
                .statusCode(200);


        String body = response.getBody().asString();

        if (!body.isBlank()) {
            JsonPath json = new JsonPath(body);
            assertEquals(json.getString("id"), createdCardId);
            assertNotNull(json.getString("holder"));
            assertNotNull(json.getString("bank"));
            assertNotNull(json.getString("card_type"));
        }
    }

    @Test(priority = 4, description = "TC_S2_010: Delete Card - Valid Request", dependsOnMethods = {"testCreateCardWithValidData", "testGetSpecificCard"})
    public void testDeleteCard() {
        if (createdCardId == null) {
            fail("No card created in previous test");
        }

        Response response = getAuthenticatedRequest()
                .when()
                .delete(CARDS_ENDPOINT + "/" + createdCardId + "/account/" + accountId);

        response.then()
                .statusCode(anyOf(is(200), is(204)));

        // Verify card is deleted by trying to get it
        Response getResponse = getAuthenticatedRequest()
                .when()
                .get(CARDS_ENDPOINT + "/" + createdCardId + "/account/" + accountId);

        getResponse.then()
                .statusCode(404);
    }

    @Test(priority = 5, description = "Create Card - Invalid Account ID (Negative Test)")
    public void testCreateCardWithInvalidAccountId() {
        String uniqueCardNumber = generateUniqueCardNumber();
        String cardPayload = getCardCreatePayload(uniqueCardNumber, 99999L);

        Response response = getAuthenticatedRequest()
                .body(cardPayload)
                .when()
                .post(CARDS_ENDPOINT);

        response.then()
                .statusCode(anyOf(is(400), is(404)))
                .body("message", notNullValue());
    }

    @Test(priority = 6, description = "Get Cards - Invalid Account ID (Negative Test)")
    public void testGetCardsWithInvalidAccountId() {
        Response response = getAuthenticatedRequest()
                .when()
                .get(CARDS_ENDPOINT + "/account/99999");

        response.then()
                .statusCode(anyOf(is(404), is(200)))
                .body("$", anyOf(instanceOf(java.util.List.class), hasKey("message")));
    }
}