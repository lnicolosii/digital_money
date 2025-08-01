package com.digitalmoney.auth_service.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private Long id;
    private String firstName;
    private String lastname;
    private String email;
    private String password;
    private String dni;
    private String phone;

    public User() {
    }

    public User(Long id, String name, String lastname, String email, String password, String dni, String phone) {
        this.id = id;
        this.firstName = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.dni = dni;
        this.phone = phone;
    }

}
