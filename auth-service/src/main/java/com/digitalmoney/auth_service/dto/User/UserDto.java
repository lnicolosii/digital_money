package com.digitalmoney.auth_service.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String dni;
    private String phone;
//    private String cvu;
//    private String alias;

    public UserDto() {
    }

    public UserDto(Long userId, String firstName, String lastName, String email, String password, String dni, String phone) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dni = dni;
        this.phone = phone;
    }
}
