package com.digitalmoney.tests;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static org.testng.Assert.*;

public class TransactionIntegrationTests extends BaseTest {

    @Test(priority = 1, description = "Realizar depósito exitoso")
    public void testDepositSuccess() {
        Map<String, Object> depositData = createDepositData(100.0);

        Response response = getAuthenticatedRequest()
                .body(depositData)
                .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
                .then()
                .statusCode(200)
                .extract().response();
        JsonPath res = response.jsonPath();
        assertEquals(res.getDouble("amount"), 100.0);
        assertEquals(res.getString("transaction_type"), "Deposit");
        assertNotNull(res.getString("realization_date"));
    }

    @Test(priority = 2, description = "Flujo completo de depósitos y consultas")
    public void testCompleteDepositFlow() {
        // 1. Realizar depósito inicial
        Map<String, Object> depositData = createDepositData(500.0);
        getAuthenticatedRequest()
                .body(depositData)
                .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
                .then()
                .statusCode(200);

        // 2. Verificar saldo después del depósito
        Response balanceResponse = getAuthenticatedRequest()
                .when()
                .get(ACCOUNTS_ENDPOINT + "/user/" + userId)
                .then()
                .statusCode(200)
                .extract().response();

        assertTrue(balanceResponse.jsonPath().getDouble("balance") >= 500.0);

        // 3. Realizar segundo depósito
        Map<String, Object> secondDepositData = createDepositData(100.0);
        getAuthenticatedRequest()
                .body(secondDepositData)
                .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
                .then()
                .statusCode(200);

        // 4. Verificar historial de transacciones
        Response historyResponse = getAuthenticatedRequest()
                .when()
                .get(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/activity")
                .then()
                .statusCode(200)
                .extract().response();

        assertTrue(historyResponse.jsonPath().getList("$").size() >= 2);
    }

    @Test(priority = 3, description = "Validar múltiples depósitos consecutivos")
    public void testMultipleDeposits() {
        // Realizar varios depósitos
        for (int i = 1; i <= 3; i++) {
            Map<String, Object> depositData = createDepositData(i * 50.0);
            getAuthenticatedRequest()
                    .body(depositData)
                    .when()
                    .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
                    .then()
                    .statusCode(200);
        }

        // Verificar que todas las transacciones se registraron
        Response response = getAuthenticatedRequest()
                .when()
                .get(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/activity")
                .then()
                .statusCode(200)
                .extract().response();

        assertTrue(response.jsonPath().getList("$").size() >= 3);
    }

    @Test(priority = 4, description = "Fallar depósito con monto negativo")
    public void testDepositNegativeAmount() {
        Map<String, Object> depositData = createDepositData(-50.0);

        getAuthenticatedRequest()
                .body(depositData)
                .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
                .then()
                .statusCode(400);
    }

    @Test(priority = 5, description = "Obtener historial de transacciones")
    public void testGetTransactionHistory() {
        // Realizar algunas transacciones primero
        Map<String, Object> depositData = createDepositData(75.0);
        getAuthenticatedRequest()
                .body(depositData)
                .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
                .then()
                .statusCode(200);

        // Obtener historial
        Response response = getAuthenticatedRequest()
                .when()
                .get(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/activity")
                .then()
                .statusCode(200)
                .extract().response();

        JsonPath res = response.jsonPath();
        assertTrue(res.getList("$").size() >= 1);
    }

    @Test(priority = 6, description = "Obtener transacción por ID")
    public void testGetTransactionById() {
        // Crear transacción
        Map<String, Object> depositData = createDepositData(25.0);
        Response transactionResponse = getAuthenticatedRequest()
                .body(depositData)
                .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
                .then()
                .statusCode(200)
                .extract().response();

        Long transactionId = transactionResponse.jsonPath().getLong("id");

        // Obtener transacción por ID
        Response response = getAuthenticatedRequest()
                .when()
                .get(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/activity/" + transactionId)
                .then()
                .statusCode(200)
                .extract().response();

        assertEquals(response.jsonPath().getLong("id"), transactionId);
        assertEquals(response.jsonPath().getDouble("amount"), 25.0);
        assertEquals(response.jsonPath().getString("transaction_type"), "Deposit");
    }

    @Test(priority = 7, description = "Fallar al obtener transacción inexistente")
    public void testGetNonExistentTransaction() {
        getAuthenticatedRequest()
                .when()
                .get(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/activity/99999")
                .then()
                .statusCode(404);
    }

    @Test(priority = 8, description = "Verificar actualización de saldo después de transacciones")
    public void testBalanceUpdateAfterTransactions() {
        // Obtener saldo inicial
        Response initialResponse = getAuthenticatedRequest()
                .when()
                .get(ACCOUNTS_ENDPOINT + "/user/" + userId)
                .then()
                .statusCode(200)
                .extract().response();

        Double initialBalance = initialResponse.jsonPath().getDouble("balance");

        // Realizar depósito
        Map<String, Object> depositData = createDepositData(150.0);
        getAuthenticatedRequest()
                .body(depositData)
                .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
                .then()
                .statusCode(200);

        // Verificar saldo actualizado
        Response finalResponse = getAuthenticatedRequest()
                .when()
                .get(ACCOUNTS_ENDPOINT + "/user/" + userId)
                .then()
                .statusCode(200)
                .extract().response();

        Double finalBalance = finalResponse.jsonPath().getDouble("balance");
        assertEquals(finalBalance, initialBalance + 150.0);
    }

    @Test(priority = 9, description = "Validar estructura de transacciones")
    public void testTransactionStructure() {
        // Realizar transacción de depósito
        Map<String, Object> depositData = createDepositData(100.0);
        getAuthenticatedRequest()
                .body(depositData)
                .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
                .then()
                .statusCode(200);

        // Obtener todas las transacciones
        Response response = getAuthenticatedRequest()
                .when()
                .get(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/activity")
                .then()
                .statusCode(200)
                .extract().response();

        assertTrue(response.jsonPath().getList("$").size() >= 1);
        // Verificar que las transacciones tienen la estructura correcta
        assertNotNull(response.jsonPath().getString("[0].id"));
        assertNotNull(response.jsonPath().getString("[0].transaction_type"));
        assertNotNull(response.jsonPath().getString("[0].amount"));
    }
}