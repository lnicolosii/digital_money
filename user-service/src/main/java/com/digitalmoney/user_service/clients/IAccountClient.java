package com.digitalmoney.user_service.clients;

import com.digitalmoney.user_service.clients.dtos.AccountDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "account-service")
public interface IAccountClient {

    @PostMapping("/accounts")
    AccountDTO createAccount(@RequestParam(name = "user_id") Long userId);

    @GetMapping("/accounts/user/{userId}")
    AccountDTO getAccountByUserId(@PathVariable(name = "userId") Long userId);

}
