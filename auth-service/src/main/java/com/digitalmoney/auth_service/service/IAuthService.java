package com.digitalmoney.auth_service.service;

import com.digitalmoney.auth_service.controller.requestDto.LoginDto;
import com.digitalmoney.auth_service.controller.requestDto.UserCreateDto;
import com.digitalmoney.auth_service.controller.responseDto.UserResponseDto;

public interface IAuthService {
    UserResponseDto signup(UserCreateDto data);

    String login(LoginDto data);

    Long validateToken(String token);
}
