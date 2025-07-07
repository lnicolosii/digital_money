package com.digitalmoney.user_service.service.impl;

import com.digitalmoney.user_service.clients.IAccountClient;
import com.digitalmoney.user_service.clients.dtos.AccountDTO;
import com.digitalmoney.user_service.controller.requestDto.UserCreateDTO;
import com.digitalmoney.user_service.controller.requestDto.UserUpdateDto;
import com.digitalmoney.user_service.dto.UserDto;
import com.digitalmoney.user_service.dto.UserResponseDto;
import com.digitalmoney.user_service.entity.User;
import com.digitalmoney.user_service.exceptions.BadRequestException;
import com.digitalmoney.user_service.exceptions.NotFoundException;
import com.digitalmoney.user_service.mappers.UserMapper;
import com.digitalmoney.user_service.repository.IUserRepository;
import com.digitalmoney.user_service.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final UserMapper mapper;
    private final IAccountClient accountClient;

    public UserService(IUserRepository userRepository, UserMapper mapper, IAccountClient accountClient) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.accountClient = accountClient;
    }

    @Transactional
    @Override
    public UserDto createUser(UserCreateDTO data) {
        log.info("Creating user with email: {}", data.getEmail());
        
        Optional<User> entityResponseDni = userRepository.findByDni(data.getDni());
        Optional<User> entityResponseEmail = userRepository.findByEmail(data.getEmail());

        if (entityResponseDni.isPresent()) {
            log.warn("User creation failed: DNI already exists {}", data.getDni());
            throw new BadRequestException("User with DNI " + data.getDni() + " already exists");
        }
        
        if (entityResponseEmail.isPresent()) {
            log.warn("User creation failed: email already exists {}", data.getEmail());
            throw new BadRequestException("User with email " + data.getEmail() + " already exists");
        }

        User userResult = mapper.mapToEntity(data);
        userResult.setEmail(data.getEmail().toLowerCase());

        User user = userRepository.save(userResult);
        log.info("User saved with ID: {}", user.getId());

        try {
            AccountDTO account = accountClient.createAccount(user.getId());
            user.setAccountId(account.getId());
            userRepository.save(user);
            
            log.info("User successfully created with email: {} and account ID: {}", data.getEmail(), account.getId());
            return mapper.mapToDto(user, account.getCvu(), account.getAlias());
        } catch (Exception e) {
            log.error("Failed to create account for user {}: {}", user.getId(), e.getMessage());
            // Rollback will happen automatically due to @Transactional
            throw new RuntimeException("Failed to create user account: " + e.getMessage(), e);
        }
    }

    @Transactional
    @Override
    public UserResponseDto updateUser(UserUpdateDto data, Long userId) {
        log.info("Updating user with ID: {}", userId);
        
        User user = getUserId(userId);
        User userUpdated = userRepository.save(updateUserObj(data, user));
        
        try {
            AccountDTO account = accountClient.getAccountByUserId(userUpdated.getId());
            log.info("User successfully updated with ID: {}", userId);
            return mapper.mapToResponseDto(userUpdated, account.getCvu(), account.getAlias());
        } catch (Exception e) {
            log.error("Failed to retrieve account for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to retrieve user account information: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findByEmail(String email) {
        log.debug("Finding user by email: {}", email);
        
        if (email == null || email.trim().isEmpty()) {
            log.warn("Find by email failed: email is null or empty");
            throw new BadRequestException("Email cannot be null or empty");
        }
        
        Optional<User> entityResponseUser = userRepository.findByEmail(email.toLowerCase());
        User user = entityResponseUser.orElse(null);
        
        if (user == null) {
            log.debug("User not found with email: {}", email);
            return null; // Return null as per original behavior for auth compatibility
        }
        
        try {
            AccountDTO account = accountClient.getAccountByUserId(user.getId());
            log.debug("User found with email: {}", email);
            return mapper.mapToDto(user, account.getCvu(), account.getAlias());
        } catch (Exception e) {
            log.error("Failed to retrieve account for user with email {}: {}", email, e.getMessage());
            throw new RuntimeException("Failed to retrieve user account information: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDto findByUserId(Long userId) {
        log.debug("Finding user by ID: {}", userId);
        
        User user = getUserId(userId);
        
        try {
            AccountDTO account = accountClient.getAccountByUserId(user.getId());
            log.debug("User found with ID: {}", userId);
            return mapper.mapToResponseDto(user, account.getCvu(), account.getAlias());
        } catch (Exception e) {
            log.error("Failed to retrieve account for user {}: {}", userId, e.getMessage());
            throw new RuntimeException("Failed to retrieve user account information: " + e.getMessage(), e);
        }
    }


    //    UTILS
    private User getUserId(Long userId) {
        if (userId == null) {
            log.warn("Get user by ID failed: userId is null");
            throw new BadRequestException("User ID cannot be null");
        }
        
        Optional<User> entityResponseUser = userRepository.findById(userId);

        if (entityResponseUser.isEmpty()) {
            log.warn("User not found with ID: {}", userId);
            throw new NotFoundException("User not found with ID: " + userId);
        }
        return entityResponseUser.get();
    }

    private User updateUserObj(UserUpdateDto data, User user) {
        if (data.getFirstName() != null) user.setFirstName(data.getFirstName());
        if (data.getLastName() != null) user.setLastName(data.getLastName());
        if (data.getEmail() != null) user.setEmail(data.getEmail());
        if (data.getPhone() != null) user.setPhone(data.getPhone());
        if (data.getDni() != null) user.setDni(data.getDni());

        return user;
    }
}
