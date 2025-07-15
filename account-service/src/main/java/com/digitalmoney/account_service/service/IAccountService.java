package com.digitalmoney.account_service.service;

import com.digitalmoney.account_service.controller.requestDto.AccountUpdateDTO;
import com.digitalmoney.account_service.controller.requestDto.DepositDTO;
import com.digitalmoney.account_service.controller.requestDto.TransferDTO;
import com.digitalmoney.account_service.dto.AccountDto;
import com.digitalmoney.account_service.dto.TransactionDto;

import java.util.List;

public interface IAccountService {
    AccountDto createAccount(Long userId);

    AccountDto updateAccount(Long userId, AccountUpdateDTO data);

    AccountDto findByUserId(Long accountId);

    List<AccountDto> getAccounts();

    TransactionDto depositMoney(Long accountId, DepositDTO data);

    TransactionDto transferMoney(Long accountId, TransferDTO data);

    List<TransactionDto> getLastFiveTransactions(Long accountId);

    List<TransactionDto> getTransactions(Long accountId);

    TransactionDto getTransaction(Long transactionId);
}
