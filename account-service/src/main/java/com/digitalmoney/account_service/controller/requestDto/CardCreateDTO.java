package com.digitalmoney.account_service.controller.requestDto;

import com.digitalmoney.account_service.entity.enums.NetworkCard;
import com.digitalmoney.account_service.entity.enums.TypeCard;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardCreateDTO {
    @NotNull
    private Long accountId;
    @NotNull
    private String holder;
    @NotNull
    private String bank;
    @NotNull
    private String expirationDate;
    @NotNull
    private Long number;
    @NotNull
    private Integer cvv;
    @NotNull
    private TypeCard cardType;
    @NotNull
    private NetworkCard network;

}
