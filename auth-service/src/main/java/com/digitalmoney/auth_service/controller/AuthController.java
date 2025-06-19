package com.digitalmoney.auth_service.controller;

import com.digitalmoney.auth_service.controller.requestDto.UserCreateDto;
import com.digitalmoney.auth_service.controller.responseDto.AuthenticateResponse;
import com.digitalmoney.auth_service.controller.requestDto.LoginDto;
import com.digitalmoney.auth_service.controller.responseDto.UserResponseDto;
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
    public ResponseEntity<?> signup(@Valid @RequestBody(required = true) UserCreateDto data) {
        try {
            String token = authService.signup(data);
            if (token != null) return ResponseEntity.status(HttpStatus.CREATED).body(new AuthenticateResponse(token));
        } catch (Exception e) {
            log.error("Error creating an user - {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.internalServerError().body("Something went wrong");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = true) LoginDto data) throws Exception {
        try {
            String token = authService.login(data);
            if (token != null) return new ResponseEntity<>(new AuthenticateResponse(token), HttpStatus.OK);
        } catch (Exception e) {
            log.info("Failed to authenticate -> {}", String.valueOf(e));
        }
        return ResponseEntity.internalServerError().body("Something went wrong");
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody Map<String, String> request) {
        try {
            String token = request.get("token");

            Long userId = authService.validateToken(token);
            Map<String, Object> response = new HashMap<>();
            response.put("id", userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.info("Failed to validate -> {}", String.valueOf(e));
        }
        return ResponseEntity.internalServerError().body("Something went wrong");
    }
}
