package com.digitalmoney.user_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String dni;
    private String phone;
    private String cvu;
    private String alias;

    public UserResponseDto() {
    }

    public UserResponseDto(Long id, String firstName, String lastName, String email, String dni, String phone, String cvu, String alias) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dni = dni;
        this.phone = phone;
        this.cvu = cvu;
        this.alias = alias;
    }
}
