package com.digitalmoney.account_service.service.impl;

import com.digitalmoney.account_service.controller.requestDto.AccountCreateDTO;
import com.digitalmoney.account_service.dto.AccountResponseDto;
import com.digitalmoney.account_service.entity.Account;
import com.digitalmoney.account_service.mappers.AccountMapper;
import com.digitalmoney.account_service.repository.IAccountRepository;
import com.digitalmoney.account_service.service.IAccountService;
import com.digitalmoney.account_service.utils.GeneratorKeys;
import jakarta.ws.rs.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AccountService implements IAccountService {
    private final AccountMapper accountMapper;
    private final IAccountRepository accountRepository;

    public AccountService(AccountMapper accountMapper, IAccountRepository accountRepository) {
        this.accountMapper = accountMapper;
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Override
    public AccountResponseDto createAccount(Long userId) throws Exception {
        Account account = new Account();
        account.setUserId(userId);

        String cvu = GeneratorKeys.generateCvu();
        while (accountRepository.findByCvu(cvu).isPresent()) {
            cvu = GeneratorKeys.generateCvu();
        }
        account.setCvu(cvu);

        String alias = GeneratorKeys.generateAlias();
        while (accountRepository.findByCvu(alias).isPresent()) {
            alias = GeneratorKeys.generateCvu();
        }
        account.setAlias(alias);
        account.setBalance(0.0);

        Account accountResponse = accountRepository.save(account);
        return accountMapper.mapToDto(accountResponse);
    }

    @Transactional(readOnly = true)
    @Override
    public AccountResponseDto findByUserId(Long id) throws Exception {
        Optional<Account> account = accountRepository.findByUserId(id);
        if (account.isEmpty()) {
            throw new NotFoundException("Account not found with id: " + id);
        }
        return accountMapper.mapToDto(account.get());
    }

}
