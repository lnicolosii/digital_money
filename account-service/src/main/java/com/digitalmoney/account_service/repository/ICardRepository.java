package com.digitalmoney.account_service.repository;

import com.digitalmoney.account_service.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findByNumber(Long cardNumber);

    List<Card> findAllByAccountId(Long accountId);

    Optional<Card> findByIdAndAccountId(Long cardId, Long accountId);
}
