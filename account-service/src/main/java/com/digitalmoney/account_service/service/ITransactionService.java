package com.digitalmoney.account_service.service;

import com.digitalmoney.account_service.controller.requestDto.DepositDTO;
import com.digitalmoney.account_service.controller.requestDto.TransferDTO;
import com.digitalmoney.account_service.dto.TransactionDto;

import java.util.List;

public interface ITransactionService {
    TransactionDto depositTransaction(Long accountId, DepositDTO data);

    TransactionDto transferTransaction(Long accountId, TransferDTO data);

    List<TransactionDto> getLastTransactions(Long accountId);

    List<TransactionDto> getTransactions(Long accountId);

    TransactionDto getTransaction(Long transactionId);
}
