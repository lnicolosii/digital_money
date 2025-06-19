package com.digitalmoney.auth_service.controller.responseDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;


@Getter
public class AuthenticateResponse {
    @NotNull
    private String accessToken;

    public AuthenticateResponse(String token) {
        this.accessToken = token;
    }
}
