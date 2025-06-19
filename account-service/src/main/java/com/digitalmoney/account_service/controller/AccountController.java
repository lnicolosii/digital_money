package com.digitalmoney.account_service.controller;

import com.digitalmoney.account_service.service.impl.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestParam(name = "user_id") Long userId) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAccountByUserId(@PathVariable Long userId) throws Exception {
        return ResponseEntity.ok(accountService.findByUserId(userId));
    }
}