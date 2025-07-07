package com.digitalmoney.account_service.service;

import com.digitalmoney.account_service.controller.requestDto.AccountUpdateDTO;
import com.digitalmoney.account_service.dto.AccountDto;

import java.util.List;

public interface IAccountService {
    AccountDto createAccount(Long userId);

    AccountDto updateAccount(Long userId, AccountUpdateDTO data);

    AccountDto findByUserId(Long accountId);

    List<AccountDto> getAccounts();

}
