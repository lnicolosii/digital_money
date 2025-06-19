package com.digitalmoney.account_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AccountResponseDto {

    @JsonProperty("account_id")
    private Long accountId;

    private String alias;

    private String cvu;

    private double balance;

}
