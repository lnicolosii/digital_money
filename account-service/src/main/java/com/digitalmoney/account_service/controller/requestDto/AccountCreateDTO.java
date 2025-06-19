package com.digitalmoney.account_service.controller.requestDto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountCreateDTO {
    @NotNull(message = "Field name is required")
    private String firstName;
    @NotNull(message = "Field lastname is required")
    private String lastName;
    @NotNull(message = "Field email is required")
    private String email;
    @NotNull(message = "Field password is required")
    private String password;
    @NotNull(message = "Field dni is required")
    private String dni;
    @NotNull(message = "Field phone is required")
    private String phone;
}
