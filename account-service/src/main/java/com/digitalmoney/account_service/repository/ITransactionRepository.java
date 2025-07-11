package com.digitalmoney.account_service.repository;


import com.digitalmoney.account_service.dto.TransactionDto;
import com.digitalmoney.account_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId ORDER BY t.realizationDate DESC LIMIT 5")
    List<Transaction> findLastFiveByAccountId(@Param("accountId") Long accountId);

    @Query("SELECT t FROM Transaction t WHERE t.account.id = :accountId ORDER BY t.realizationDate DESC")
    List<Transaction> findAllSorted(Long accountId);

}
