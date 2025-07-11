package com.digitalmoney.account_service.service;

import com.digitalmoney.account_service.controller.requestDto.AccountUpdateDTO;
import com.digitalmoney.account_service.controller.requestDto.DepositDto;
import com.digitalmoney.account_service.dto.AccountDto;
import com.digitalmoney.account_service.dto.TransactionDto;

import java.util.List;

public interface IAccountService {
    AccountDto createAccount(Long userId);

    AccountDto updateAccount(Long userId, AccountUpdateDTO data);

    AccountDto findByUserId(Long accountId);

    List<AccountDto> getAccounts();

    TransactionDto depositMoney(Long accountId, DepositDto data);

    List<TransactionDto> getLastFiveTransactions(Long accountId);

    List<TransactionDto> getTransactions(Long accountId);

    TransactionDto getTransaction(Long transactionId);
}
