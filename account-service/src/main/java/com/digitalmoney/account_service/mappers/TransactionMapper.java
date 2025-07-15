package com.digitalmoney.account_service.mappers;

import com.digitalmoney.account_service.dto.AccountDto;
import com.digitalmoney.account_service.dto.TransactionDto;
import com.digitalmoney.account_service.entity.Account;
import com.digitalmoney.account_service.entity.Transaction;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    public TransactionMapper(ObjectMapper objectMapper) {
    }

    public TransactionDto mapToDto(Transaction data) {
        TransactionDto dto = new TransactionDto();
        dto.setId(data.getId());
        dto.setDescription(data.getDescription());
        dto.setOrigin(data.getOrigin());
        dto.setAmount(data.getAmount());
        dto.setDestination(data.getDestination());
        dto.setRealizationDate(data.getRealizationDate());
        dto.setTransactionType(data.getTransactionType());
        return dto;
    }


}
