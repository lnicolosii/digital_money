package com.digitalmoney.account_service.repository;

import com.digitalmoney.account_service.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByCvu(String cvu);
    Optional<Account> findByUserId(Long userId);
}
