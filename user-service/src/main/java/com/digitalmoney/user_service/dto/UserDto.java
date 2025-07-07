package com.digitalmoney.user_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String dni;
    private String phone;
    private String cvu;
    private String alias;

    public UserDto() {
    }

    public UserDto(Long id, String firstName, String lastName, String email, String password, String dni, String phone, String cvu, String alias) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dni = dni;
        this.phone = phone;
        this.cvu = cvu;
        this.alias = alias;
    }
}
