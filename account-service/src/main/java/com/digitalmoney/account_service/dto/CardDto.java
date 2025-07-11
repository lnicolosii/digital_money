package com.digitalmoney.account_service.dto;

import com.digitalmoney.account_service.entity.enums.NetworkCard;
import com.digitalmoney.account_service.entity.enums.TypeCard;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardDto {
    private Long id;
    private String holder;
    private String bank;
    @JsonProperty("expiration_date")
    private String expirationDate;
    private Long number;
    private Integer cvv;
    @JsonProperty("card_type")
    private TypeCard cardType;
    private NetworkCard network;
}
