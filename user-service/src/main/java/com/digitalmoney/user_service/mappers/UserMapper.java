package com.digitalmoney.user_service.mappers;

import com.digitalmoney.user_service.controller.requestDto.UserCreateDTO;
import com.digitalmoney.user_service.dto.UserDto;
import com.digitalmoney.user_service.dto.UserResponseDto;
import com.digitalmoney.user_service.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final ObjectMapper objectMapper;

    @Autowired
    public UserMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public UserDto mapToDto(User userEntity, String cvu, String alias) {
        UserDto dto = new UserDto();
        dto.setUserId(userEntity.getUserId());
        dto.setFirstName(userEntity.getFirstName());
        dto.setPassword(userEntity.getPassword());
        dto.setLastName(userEntity.getLastName());
        dto.setEmail(userEntity.getEmail());
        dto.setDni(userEntity.getDni());
        dto.setPhone(userEntity.getPhone());
        dto.setCvu(cvu);
        dto.setAlias(alias);
        return dto;
    }

    public UserResponseDto mapToResponseDto(User userEntity, String cvu, String alias) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUserId(userEntity.getUserId());
        dto.setFirstName(userEntity.getFirstName());
        dto.setLastName(userEntity.getLastName());
        dto.setEmail(userEntity.getEmail());
        dto.setDni(userEntity.getDni());
        dto.setPhone(userEntity.getPhone());
        dto.setCvu(cvu);
        dto.setAlias(alias);
        return dto;
    }

    public User mapToEntity(UserCreateDTO data) {
        return objectMapper.convertValue(data, User.class);
    }


}
