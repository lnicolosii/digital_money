package com.digitalmoney.account_service.controller.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountUpdateDTO {
    private String alias;
}
