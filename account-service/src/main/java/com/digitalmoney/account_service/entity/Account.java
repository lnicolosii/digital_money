package com.digitalmoney.account_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 22)
    private String cvu;
    @Column(nullable = false, unique = true)
    private String alias;
    @Column(nullable = false)
    private Double balance = 0.0;
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "account")
    @JsonIgnore
    private List<Card> cards;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<Transaction> transactions;

    public Account addBalance(Double value) {
        this.balance = this.balance + value;
        return this;
    }

    public Account removeBalance(Double value) {
        this.balance = this.balance - value;
        return this;
    }
}
