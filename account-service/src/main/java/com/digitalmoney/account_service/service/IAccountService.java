package com.digitalmoney.account_service.service;

import com.digitalmoney.account_service.dto.AccountResponseDto;

public interface IAccountService {
    AccountResponseDto createAccount(Long userId) throws Exception;

    AccountResponseDto findByUserId(Long accountId) throws Exception;

}
