package com.digitalmoney.user_service.service.impl;

import com.digitalmoney.user_service.clients.IAccountClient;
import com.digitalmoney.user_service.clients.dtos.AccountDTO;
import com.digitalmoney.user_service.controller.requestDto.UserCreateDTO;
import com.digitalmoney.user_service.dto.UserDto;
import com.digitalmoney.user_service.dto.UserResponseDto;
import com.digitalmoney.user_service.entity.User;
import com.digitalmoney.user_service.mappers.UserMapper;
import com.digitalmoney.user_service.repository.IUserRepository;
import com.digitalmoney.user_service.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
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
    public UserDto createUser(UserCreateDTO data) throws Exception {
        Optional<User> entityResponseDni = userRepository.findByDni(data.getDni());
        Optional<User> entityResponseEmail = userRepository.findByEmail(data.getEmail());

        if (entityResponseDni.isPresent() || entityResponseEmail.isPresent()) {
            throw new Exception("The user is already registered");
        }

        User userResult = mapper.mapToEntity(data);
        userResult.setEmail(data.getEmail().toLowerCase());

        User user = userRepository.save(userResult);

        AccountDTO account = accountClient.createAccount(user.getUserId());
        user.setAccountId(account.getAccountId());

        userRepository.save(user);
        return mapper.mapToDto(user, account.getCvu(), account.getAlias());
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto findByEmail(String email) {
        Optional<User> entityResponseUser = userRepository.findByEmail(email);
        User user = entityResponseUser.orElse(null);
        UserDto userDto = null;
        if (user != null) {
            AccountDTO account = accountClient.getAccountByUserId(user.getUserId());
            userDto = mapper.mapToDto(user, account.getCvu(), account.getAlias());
        }
        return userDto;
    }

    @Override
    public UserResponseDto findByUserId(String userId) throws Exception {
        Optional<User> entityResponseUser = userRepository.findByUserId(Long.parseLong(userId));

        if (entityResponseUser.isEmpty()) {
            throw new Exception("User not found");
        }
        User user = entityResponseUser.get();

        AccountDTO account = accountClient.getAccountByUserId(user.getUserId());

        return mapper.mapToResponseDto(user, account.getCvu(), account.getAlias());
    }
}
