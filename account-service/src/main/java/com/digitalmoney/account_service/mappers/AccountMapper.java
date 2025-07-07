package com.digitalmoney.account_service.mappers;

import com.digitalmoney.account_service.dto.AccountDto;
import com.digitalmoney.account_service.entity.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    @Autowired
    public AccountMapper(ObjectMapper objectMapper) {
    }

    public AccountDto mapToDto(Account data) {
        AccountDto dto = new AccountDto();
        dto.setId(data.getId());
        dto.setCvu(data.getCvu());
        dto.setAlias(data.getAlias());
        dto.setUserId(data.getUserId());
        dto.setBalance(data.getBalance());
        return dto;
    }


}
