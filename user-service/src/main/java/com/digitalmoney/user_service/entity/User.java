package com.digitalmoney.user_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;
    @Column(nullable = false, name = "first_name")
    private String firstName;
    @Column(nullable = false, name = "last_name")
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String dni;
    @Column(nullable = false)
    private String phone;

    @Column(name = "account_id", unique = true)
    private Long accountId;

    public User() {
    }

    public User(Long userId, String firstName, String lastName, String email, String password, String dni, String phone, Long accountId) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dni = dni;
        this.phone = phone;
        this.accountId = accountId;
    }
}
