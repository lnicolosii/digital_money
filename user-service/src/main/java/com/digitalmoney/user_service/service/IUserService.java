package com.digitalmoney.user_service.service;

import com.digitalmoney.user_service.controller.requestDto.UserCreateDTO;
import com.digitalmoney.user_service.dto.UserDto;
import com.digitalmoney.user_service.dto.UserResponseDto;

public interface IUserService {
    UserDto createUser(UserCreateDTO data) throws Exception;

    UserDto findByEmail(String email);

    UserResponseDto findByUserId(String findById) throws Exception;
}
