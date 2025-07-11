package com.digitalmoney.account_service.service;

import com.digitalmoney.account_service.controller.requestDto.DepositDto;
import com.digitalmoney.account_service.dto.TransactionDto;

import java.util.List;

public interface ITransactionService {
    TransactionDto depositTransaction(Long accountId, DepositDto data);

    List<TransactionDto> getLastTransactions(Long accountId);

    List<TransactionDto> getTransactions(Long accountId);

    TransactionDto getTransaction(Long transactionId);
}
