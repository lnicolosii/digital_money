package com.digitalmoney.account_service.controller.requestDto;

import com.digitalmoney.account_service.entity.enums.NetworkCard;
import com.digitalmoney.account_service.entity.enums.TypeCard;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardUpdateDTO {
    private String holder;
    private String bank;
    private String expirationDate;
    private Long number;
    private Integer cvv;
    private TypeCard cardType;
    private NetworkCard network;
}
