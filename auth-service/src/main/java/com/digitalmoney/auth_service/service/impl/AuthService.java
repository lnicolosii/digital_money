package com.digitalmoney.auth_service.service.impl;

import com.digitalmoney.auth_service.controller.requestDto.LoginDto;
import com.digitalmoney.auth_service.controller.requestDto.UserCreateDto;
import com.digitalmoney.auth_service.controller.responseDto.UserResponseDto;
import com.digitalmoney.auth_service.dto.User.UserDto;
import com.digitalmoney.auth_service.exceptions.InvalidCredentialsException;
import com.digitalmoney.auth_service.exceptions.TokenValidationException;
import com.digitalmoney.auth_service.exceptions.UserAlreadyExistsException;
import com.digitalmoney.auth_service.exceptions.UserNotFoundException;
import com.digitalmoney.auth_service.mappers.UserMapper;
import com.digitalmoney.auth_service.repository.FeignUserRepository;
import com.digitalmoney.auth_service.jwt.JwtUtil;
import com.digitalmoney.auth_service.service.IAuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuthService implements IAuthService {
    @Autowired
    private FeignUserRepository userRepository;
    @Autowired
    private JwtUtil jwtUtil;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
    private final UserMapper mapper;

    public AuthService(FeignUserRepository userRepository, JwtUtil jwtUtil, UserMapper mapper) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.mapper = mapper;
    }

    @Override
    public UserResponseDto signup(UserCreateDto data) {
        log.info("Attempting to register user with email: {}", data.getEmail());

        UserDto userExists = userRepository.findByEmail(data.getEmail()).getBody();
        if (userExists != null) {
            log.warn("User registration failed: user already exists with email {}", data.getEmail());
            throw new UserAlreadyExistsException("User with email " + data.getEmail() + " already exists");
        }

        data.setPassword(bCryptPasswordEncoder.encode(data.getPassword()));
        UserDto user = userRepository.createUser(data);

        if (user == null) {
            log.error("Failed to create user with email: {}", data.getEmail());
            throw new RuntimeException("Failed to create user");
        }

        log.info("User successfully registered with email: {}", data.getEmail());
        return mapper.mapToResponseDto(user);
    }

    @Override
    public String login(LoginDto data) {
        log.info("Attempting login for user with email: {}", data.getEmail());

        UserDto user = userRepository.findByEmail(data.getEmail()).getBody();
        if (user == null) {
            log.warn("Login failed: user not found with email {}", data.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        // CRITICAL FIX: Verify password before generating token
        if (!bCryptPasswordEncoder.matches(data.getPassword(), user.getPassword())) {
            log.warn("Login failed: invalid password for user with email {}", data.getEmail());
            throw new InvalidCredentialsException("Invalid email or password");
        }

        log.info("User successfully authenticated with email: {}", data.getEmail());
        return jwtUtil.generateToken(user.getId(), user);
    }

    @Override
    public Long validateToken(String token) {
        log.debug("Attempting to validate token");

        String email = jwtUtil.extractUserEmail(token);
        if (email == null || email.isEmpty()) {
            log.warn("Token validation failed: unable to extract email from token");
            throw new TokenValidationException("Invalid token format");
        }

        UserDto user = userRepository.findByEmail(email).getBody();
        if (user == null) {
            log.warn("Token validation failed: user not found with email {}", email);
            throw new UserNotFoundException("User not found for token validation");
        }

        if (!jwtUtil.validateToken(token, user)) {
            log.warn("Token validation failed: invalid signature or expired for user {}", email);
            throw new TokenValidationException("Token is invalid or expired");
        }

        log.debug("Token successfully validated for user with email: {}", email);
        return user.getId();
    }

}
