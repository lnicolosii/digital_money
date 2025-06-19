package com.digitalmoney.user_service.controller;

import com.digitalmoney.user_service.controller.requestDto.UserCreateDTO;
import com.digitalmoney.user_service.dto.UserDto;
import com.digitalmoney.user_service.dto.UserResponseDto;
import com.digitalmoney.user_service.service.IUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserCreateDTO data) throws Exception {
        log.info(String.valueOf(data));
        if (Objects.isNull(data)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.createUser(data));
    }

    @GetMapping
    public ResponseEntity<UserDto> findByEmail(@RequestParam String email) {
        log.info(String.valueOf(email));
        return new ResponseEntity<>(this.userService.findByEmail(email), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> findByUserId(@PathVariable String userId) throws Exception {
        log.info(String.valueOf(userId));
        return new ResponseEntity<>(this.userService.findByUserId(userId), HttpStatus.OK);
    }

}
