package com.digitalmoney.user_service.controller;

import com.digitalmoney.user_service.controller.requestDto.UserCreateDTO;
import com.digitalmoney.user_service.controller.requestDto.UserUpdateDto;
import com.digitalmoney.user_service.dto.UserDto;
import com.digitalmoney.user_service.dto.UserResponseDto;
import com.digitalmoney.user_service.service.IUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserCreateDTO data) {
        log.info("User creation request received for email: {}", data.getEmail());
        UserDto user = this.userService.createUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@Valid @RequestBody UserUpdateDto data, @PathVariable Long userId) {
        log.info("User update request received for user ID: {}", userId);
        UserResponseDto user = this.userService.updateUser(data, userId);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<UserDto> findByEmail(@RequestParam String email) {
        log.info("Find user by email request: {}", email);
        UserDto user = this.userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findByUserId(@PathVariable Long userId) {
        log.info("Find user by ID request: {}", userId);
        UserResponseDto user = this.userService.findByUserId(userId);
        return ResponseEntity.ok(user);
    }

}
