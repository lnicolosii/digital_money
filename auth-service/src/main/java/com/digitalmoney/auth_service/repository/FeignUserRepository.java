package com.digitalmoney.auth_service.repository;

import com.digitalmoney.auth_service.controller.requestDto.UserCreateDto;
import com.digitalmoney.auth_service.controller.responseDto.UserResponseDto;
import com.digitalmoney.auth_service.dto.User.UserDto;
import com.digitalmoney.auth_service.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@FeignClient(name = "user-service")
public interface FeignUserRepository {
    @GetMapping(value = "/users")
    ResponseEntity<UserDto> findByEmail(@RequestParam String email);

    @PostMapping(value = "/users")
    UserDto createUser(UserCreateDto data);
}
