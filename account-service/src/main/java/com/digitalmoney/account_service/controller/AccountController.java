package com.digitalmoney.account_service.controller;

import com.digitalmoney.account_service.controller.requestDto.AccountUpdateDTO;
import com.digitalmoney.account_service.controller.requestDto.DepositDTO;
import com.digitalmoney.account_service.controller.requestDto.TransferDTO;
import com.digitalmoney.account_service.dto.AccountDto;
import com.digitalmoney.account_service.dto.TransactionDto;
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

    @PostMapping("/{accountId}/deposit")
    public ResponseEntity<TransactionDto> depositMoney(@PathVariable Long accountId, @RequestBody DepositDTO data) {
        log.info("Deposit money by account ID request: {}", accountId);
        TransactionDto transaction = accountService.depositMoney(accountId, data);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/{accountId}/transfer")
    public ResponseEntity<TransactionDto> transferMoney(@PathVariable Long accountId, @RequestBody TransferDTO data) {
        log.info("Deposit money by account ID request: {}", accountId);
        TransactionDto transaction = accountService.transferMoney(accountId, data);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{accountId}/activity")
    public ResponseEntity<List<TransactionDto>> getAllActivities(@PathVariable Long accountId) {
        log.info("Get all transactions by account ID request: {}", accountId);
        return ResponseEntity.ok(accountService.getTransactions(accountId));
    }

    @GetMapping("/{accountId}/recent-activity")
    public ResponseEntity<List<TransactionDto>> getRecentActivities(@PathVariable Long accountId) {
        log.info("Get first five transactions by account ID request: {}", accountId);
        return ResponseEntity.ok(accountService.getLastFiveTransactions(accountId));
    }

    @GetMapping("/{accountId}/activity/{transactionId}")
    public ResponseEntity<TransactionDto> getActivity(@PathVariable Long accountId, @PathVariable Long transactionId) {
        log.info("Get specific transaction by account ID request: {}", accountId);
        TransactionDto transaction = accountService.getTransaction(transactionId);
        return ResponseEntity.ok(transaction);
    }
}