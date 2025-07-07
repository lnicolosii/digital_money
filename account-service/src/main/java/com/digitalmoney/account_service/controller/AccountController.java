package com.digitalmoney.account_service.controller;

import com.digitalmoney.account_service.controller.requestDto.AccountUpdateDTO;
import com.digitalmoney.account_service.dto.AccountDto;
import com.digitalmoney.account_service.service.impl.AccountService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDto> createAccount(@RequestParam(name = "user_id") Long userId) {
        log.info("Account creation request for user ID: {}", userId);
        AccountDto account = accountService.createAccount(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAccounts() {
        log.info("Get all accounts request");
        List<AccountDto> accounts = accountService.getAccounts();
        return ResponseEntity.ok(accounts);
    }

    @PatchMapping("/user/{userId}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable Long userId, @Valid @RequestBody AccountUpdateDTO data) {
        log.info("Account update request for user ID: {}", userId);
        AccountDto account = accountService.updateAccount(userId, data);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<AccountDto> getAccountByUserId(@PathVariable Long userId) {
        log.info("Get account by user ID request: {}", userId);
        AccountDto account = accountService.findByUserId(userId);
        return ResponseEntity.ok(account);
    }
}