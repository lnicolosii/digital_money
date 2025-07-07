package com.digitalmoney.auth_service.controller;

import com.digitalmoney.auth_service.controller.requestDto.UserCreateDto;
import com.digitalmoney.auth_service.controller.responseDto.AuthenticateResponse;
import com.digitalmoney.auth_service.controller.requestDto.LoginDto;
import com.digitalmoney.auth_service.controller.responseDto.UserResponseDto;
import com.digitalmoney.auth_service.exceptions.TokenValidationException;
import com.digitalmoney.auth_service.service.IAuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    @Autowired
    private final IAuthService authService;

    @Autowired
    public AuthController(IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> signup(@Valid @RequestBody UserCreateDto data) {
        log.info("User registration request received for email: {}", data.getEmail());
        UserResponseDto user = authService.signup(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticateResponse> login(@Valid @RequestBody LoginDto data) {
        log.info("Login request received for email: {}", data.getEmail());
        String token = authService.login(data);
        return ResponseEntity.ok(new AuthenticateResponse(token));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        log.debug("Token validation request received");

        if (token == null || token.trim().isEmpty()) {
            throw new TokenValidationException("Token is required");
        }

        Long userId = authService.validateToken(token);
        Map<String, Object> response = new HashMap<>();
        response.put("id", userId);
        return ResponseEntity.ok(response);
    }
}
