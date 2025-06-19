package com.digitalmoney.auth_service.service;

import com.digitalmoney.auth_service.controller.requestDto.LoginDto;
import com.digitalmoney.auth_service.controller.requestDto.UserCreateDto;

public interface IAuthService {
    String signup(UserCreateDto data) throws Exception;

    String login(LoginDto data) throws Exception;

    Long validateToken(String token) throws Exception;
}
