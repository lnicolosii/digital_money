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
public class AccountDto {
    private Long id;
    @JsonProperty("user_id")
    private Long userId;
    private String alias;
    private String cvu;
    private double balance;

}
