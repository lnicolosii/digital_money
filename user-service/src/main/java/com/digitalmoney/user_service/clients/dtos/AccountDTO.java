package com.digitalmoney.user_service.clients.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String alias;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cvu;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private double balance;
}
