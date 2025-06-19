package com.digitalmoney.auth_service.mappers;

import com.digitalmoney.auth_service.controller.requestDto.UserCreateDto;
import com.digitalmoney.auth_service.dto.User.UserDto;
import com.digitalmoney.auth_service.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    @Autowired
    private ObjectMapper objectMapper;

    public UserDto createUserMapper(UserCreateDto userEntity) {
        UserDto dto = new UserDto();
        dto.setFirstName(userEntity.getFirstName());
        dto.setLastName(userEntity.getLastName());
        dto.setEmail(userEntity.getEmail());
        dto.setDni(userEntity.getDni());
        dto.setPhone(userEntity.getPhone());
        return dto;
    }

    public UserDto mapToDto(User userEntity) {
        UserDto dto = new UserDto();
        dto.setUserId(userEntity.getUserId());
        dto.setFirstName(userEntity.getFirstName());
        dto.setPassword(userEntity.getPassword());
        dto.setLastName(userEntity.getLastname());
        dto.setEmail(userEntity.getEmail());
        dto.setDni(userEntity.getDni());
        dto.setPhone(userEntity.getPhone());
        return dto;
    }
}
