package com.digitalmoney.account_service.mappers;

import com.digitalmoney.account_service.dto.AccountResponseDto;
import com.digitalmoney.account_service.entity.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    @Autowired
    public AccountMapper(ObjectMapper objectMapper) {
    }

    public AccountResponseDto mapToDto(Account data) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setAccountId(data.getAccountId());
        dto.setCvu(data.getCvu());
        dto.setAlias(data.getAlias());
        dto.setBalance(data.getBalance());
        return dto;
    }


}
