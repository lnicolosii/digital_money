package com.digitalmoney.account_service.controller.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferDTO {
    private String cvu;
    private String alias;
    private Double amount;
}
