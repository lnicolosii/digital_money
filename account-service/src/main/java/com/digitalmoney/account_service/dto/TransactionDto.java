package com.digitalmoney.account_service.dto;

import com.digitalmoney.account_service.entity.enums.TransactionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    private Double amount;
    @JsonProperty("realization_date")
    private LocalDateTime realizationDate;
    private String origin;
    private String description;
    private String destination;
    @JsonProperty("transaction_type")
    private TransactionType transactionType;
    private AccountDto account;
}
