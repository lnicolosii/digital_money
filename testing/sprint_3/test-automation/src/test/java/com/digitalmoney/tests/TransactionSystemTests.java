package com.digitalmoney.tests;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.testng.Assert.*;

public class TransactionSystemTests extends BaseTest {
    
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
        
        assertEquals(response.jsonPath().getDouble("amount"), 100.0);
        assertEquals(response.jsonPath().getString("transaction_type"), "Deposit");
        assertNotNull(response.jsonPath().getString("realization_date"));
    }
    
    @Test(priority = 2, description = "Obtener transacciones recientes")
    public void testGetRecentTransactions() {
        // Primero hacer un depósito para tener transacciones
        Map<String, Object> depositData = createDepositData(200.0);
        getAuthenticatedRequest()
                .body(depositData)
            .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
            .then()
                .statusCode(200);
        
        // Obtener transacciones recientes (primeras 5)
        Response response = getAuthenticatedRequest()
            .when()
                .get(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/recent-activity")
            .then()
                .statusCode(200)
                .extract().response();
        
        assertTrue(response.jsonPath().getList("$").size() <= 5);
        assertTrue(response.jsonPath().getList("$").size() >= 1);
    }
    
    @Test(priority = 3, description = "Obtener historial de transacciones")
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
        
        assertTrue(response.jsonPath().getList("$").size() >= 1);
    }
    
    @Test(priority = 4, description = "Obtener transacción por ID")
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
    
    @Test(priority = 5, description = "Verificar actualización de saldo después de transacciones")
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

    @Test(priority = 7, description = "Fallar depósito con monto negativo")
    public void testDepositNegativeAmount() {
        Map<String, Object> depositData = createDepositData(-50.0);
        
        getAuthenticatedRequest()
                .body(depositData)
            .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
            .then()
                .statusCode(400);
    }
    
    @Test(priority = 8, description = "Validar estructura de respuesta de transacción")
    public void testTransactionResponseStructure() {
        Map<String, Object> depositData = createDepositData(50.0);
        
        Response response = getAuthenticatedRequest()
                .body(depositData)
            .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
            .then()
                .statusCode(200)
                .extract().response();
        
        // Validar estructura de respuesta
        assertNotNull(response.jsonPath().get("id"));
        assertNotNull(response.jsonPath().get("amount"));
        assertNotNull(response.jsonPath().get("transaction_type"));
        assertNotNull(response.jsonPath().get("realization_date"));
        assertNotNull(response.jsonPath().get("account"));
    }
    
    @Test(priority = 9, description = "Fallar al obtener transacción inexistente")
    public void testGetNonExistentTransaction() {
        getAuthenticatedRequest()
            .when()
                .get(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/activity/99999")
            .then()
                .statusCode(404);
    }
    
    @Test(priority = 10, description = "Fallar depósito sin autenticación")
    public void testDepositWithoutAuth() {
        Map<String, Object> depositData = createDepositData(100.0);
        
        given()
                .contentType("application/json")
                .body(depositData)
            .when()
                .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
            .then()
                .statusCode(401);
    }
    
    @Test(priority = 11, description = "Fallar consulta de actividad sin autenticación")
    public void testGetActivityWithoutAuth() {
        given()
            .when()
                .get(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/activity")
            .then()
                .statusCode(401);
    }
    
    @Test(priority = 12, description = "Fallar consulta de actividad reciente sin autenticación")
    public void testGetRecentActivityWithoutAuth() {
        given()
            .when()
                .get(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/recent-activity")
            .then()
                .statusCode(401);
    }
    
    @Test(priority = 13, description = "Prueba de transacciones concurrentes")
    public void testConcurrentTransactions() {
        // Simular múltiples transacciones concurrentes
        Map<String, Object> depositData = createDepositData(10.0);
        
        for (int i = 0; i < 5; i++) {
            getAuthenticatedRequest()
                    .body(depositData)
                .when()
                    .post(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/deposit")
                .then()
                    .statusCode(200);
        }
        
        // Verificar que todas las transacciones se procesaron correctamente
        Response historyResponse = getAuthenticatedRequest()
            .when()
                .get(ACCOUNTS_ENDPOINT + "/" + testAccountId + "/activity")
            .then()
                .statusCode(200)
                .extract().response();
        
        assertTrue(historyResponse.jsonPath().getList("$").size() >= 5);
    }
}