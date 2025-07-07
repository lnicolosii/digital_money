package com.digitalmoney.account_service.service.impl;

import com.digitalmoney.account_service.controller.requestDto.AccountUpdateDTO;
import com.digitalmoney.account_service.dto.AccountDto;
import com.digitalmoney.account_service.entity.Account;
import com.digitalmoney.account_service.mappers.AccountMapper;
import com.digitalmoney.account_service.repository.IAccountRepository;
import com.digitalmoney.account_service.service.IAccountService;
import com.digitalmoney.account_service.exceptions.BadRequestException;
import com.digitalmoney.account_service.exceptions.NotFoundException;
import com.digitalmoney.account_service.utils.GeneratorKeys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AccountService implements IAccountService {
    private final AccountMapper accountMapper;
    private final IAccountRepository accountRepository;

    public AccountService(AccountMapper accountMapper, IAccountRepository accountRepository) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public AccountDto createAccount(Long userId) {
        log.info("Creating account for user ID: {}", userId);
        
        if (userId == null) {
            log.warn("Account creation failed: userId is null");
            throw new BadRequestException("User ID cannot be null");
        }

        Account account = new Account();
        account.setUserId(userId);

        // Generate unique CVU
        String cvu = GeneratorKeys.generateCvu();
        while (accountRepository.findByCvu(cvu).isPresent()) {
            cvu = GeneratorKeys.generateCvu();
        }
        account.setCvu(cvu);

        // CRITICAL BUG FIX: Generate unique alias properly
        String alias = GeneratorKeys.generateAlias();
        while (accountRepository.findByAlias(alias).isPresent()) { // Fixed: was findByCvu
            alias = GeneratorKeys.generateAlias(); // Fixed: was generateCvu
        }
        account.setAlias(alias);
        account.setBalance(0.0);

        Account accountResponse = accountRepository.save(account);
        log.info("Account successfully created for user ID: {} with CVU: {} and alias: {}", 
                userId, cvu, alias);
        return accountMapper.mapToDto(accountResponse);
    }

    @Override
    public AccountDto updateAccount(Long userId, AccountUpdateDTO data) {
        log.info("Updating account for user ID: {}", userId);
        
        if (data == null || data.getAlias() == null || data.getAlias().trim().isEmpty()) {
            log.warn("Account update failed: invalid alias data");
            throw new BadRequestException("Alias cannot be null or empty");
        }
        
        Account account = getAccountByUserId(userId);
        
        // Validate alias uniqueness if it's being changed
        if (!account.getAlias().equals(data.getAlias())) {
            Optional<Account> existingAccount = accountRepository.findByAlias(data.getAlias());
            if (existingAccount.isPresent()) {
                log.warn("Account update failed: alias already exists {}", data.getAlias());
                throw new BadRequestException("Alias " + data.getAlias() + " is already in use");
            }
        }

        account.setAlias(data.getAlias());
        Account savedAccount = accountRepository.save(account);
        log.info("Account successfully updated for user ID: {} with new alias: {}", userId, data.getAlias());
        return accountMapper.mapToDto(savedAccount);
    }

    @Transactional(readOnly = true)
    @Override
    public AccountDto findByUserId(Long id) {
        log.debug("Finding account by user ID: {}", id);
        Account account = getAccountByUserId(id);
        return accountMapper.mapToDto(account);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AccountDto> getAccounts() {
        log.debug("Retrieving all accounts");
        List<Account> accounts = accountRepository.findAll();
        List<AccountDto> accountsDto = new ArrayList<>();
        for (Account account : accounts) {
            accountsDto.add(accountMapper.mapToDto(account));
        }
        log.debug("Retrieved {} accounts", accountsDto.size());
        return accountsDto;
    }

    //UTILS
    Account getAccountByUserId(Long userId) {
        if (userId == null) {
            log.warn("Get account by user ID failed: userId is null");
            throw new BadRequestException("User ID cannot be null");
        }
        
        Optional<Account> account = accountRepository.findByUserId(userId);
        if (account.isEmpty()) {
            log.warn("Account not found for user ID: {}", userId);
            throw new NotFoundException("Account not found for user ID: " + userId);
        }
        return account.get();
    }
}
