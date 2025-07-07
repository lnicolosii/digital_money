package com.digitalmoney.account_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String holder;
    @Column
    private String bank;
    @Column(nullable = false)
    private String expirationDate;
    @Column(nullable = false)
    private Long number;
    @Column(nullable = false)
    private Integer cvv;
    @Enumerated(EnumType.STRING)
    @Column
    private TypeCard cardType;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private Account account;

}
