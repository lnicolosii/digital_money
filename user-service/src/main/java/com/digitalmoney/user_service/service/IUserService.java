package com.digitalmoney.user_service.service;

import com.digitalmoney.user_service.controller.requestDto.UserCreateDTO;
import com.digitalmoney.user_service.controller.requestDto.UserUpdateDto;
import com.digitalmoney.user_service.dto.UserDto;
import com.digitalmoney.user_service.dto.UserResponseDto;

public interface IUserService {
    UserDto createUser(UserCreateDTO data);

    UserResponseDto updateUser(UserUpdateDto data, Long userId);

    UserDto findByEmail(String email);

    UserResponseDto findByUserId(Long findById);
}
