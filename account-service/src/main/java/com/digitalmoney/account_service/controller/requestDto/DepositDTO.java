package com.digitalmoney.account_service.controller.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepositDTO {
    private Long cardId;
    private Double amount;
}
