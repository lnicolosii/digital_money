package com.digitalmoney.account_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountDto {
    private Long accountId;
    private Long userId;
    private String cvu;
    private String alias;

    public AccountDto() {
    }

    public AccountDto(Long accountId, Long userId, String cvu, String alias) {
        this.accountId = accountId;
        this.userId = userId;
        this.cvu = cvu;
        this.alias = alias;
    }
}
