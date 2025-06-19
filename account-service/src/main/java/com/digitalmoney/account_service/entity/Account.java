package com.digitalmoney.account_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Long accountId;
    @Column(nullable = false, unique = true, length = 22)
    private String cvu;
    @Column(nullable = false, unique = true)
    private String alias;
    @Column(nullable = false)
    private Double balance = 0.0;
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    public Account() {
    }

    public Account(Long accountId, String cvu, String alias, Long userId) {
        this.accountId = accountId;
        this.cvu = cvu;
        this.alias = alias;
        this.userId = userId;
    }
}
