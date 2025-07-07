package com.digitalmoney.user_service.controller.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
    private String firstName;
    private String lastName;
    private String email;
    private String dni;
    private String phone;
}
