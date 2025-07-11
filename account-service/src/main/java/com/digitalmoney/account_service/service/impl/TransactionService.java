package com.digitalmoney.account_service.service.impl;

import com.digitalmoney.account_service.controller.requestDto.DepositDto;
import com.digitalmoney.account_service.dto.TransactionDto;
import com.digitalmoney.account_service.entity.Account;
import com.digitalmoney.account_service.entity.Card;
import com.digitalmoney.account_service.entity.Transaction;
import com.digitalmoney.account_service.entity.enums.TransactionType;
import com.digitalmoney.account_service.exceptions.BadRequestException;
import com.digitalmoney.account_service.exceptions.NotFoundException;
import com.digitalmoney.account_service.mappers.TransactionMapper;
import com.digitalmoney.account_service.repository.IAccountRepository;
import com.digitalmoney.account_service.repository.ICardRepository;
import com.digitalmoney.account_service.repository.ITransactionRepository;
import com.digitalmoney.account_service.service.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService implements ITransactionService {
    @Autowired
    private TransactionMapper transactionMapper;
    private ITransactionRepository transactionRepository;
    private ICardRepository cardRepository;
    private IAccountRepository accountRepository;

    public TransactionService(ITransactionRepository transactionRepository, ICardRepository cardRepository, IAccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public TransactionDto depositTransaction(Long accountId, DepositDto data) {
        Optional<Card> cardResult = cardRepository.findById(data.getCardId());
        Optional<Account> accountResult = accountRepository.findById(accountId);

        if (cardResult.isEmpty()) {
            throw new NotFoundException("Card not found : " + data.getCardId());
        }
        if (accountResult.isEmpty()) {
            throw new NotFoundException("Account not found : " + accountId);
        }

        if (data.getAmount() == 0.0) {
            throw new BadRequestException("The amount can't be 0. Please enter a valid amount");
        } else if (data.getAmount() < 0.0) {
            throw new BadRequestException("The amount can't be negative. Please enter a valid amount");
        }

        Account account = accountResult.get();
        Card card = cardResult.get();
        Transaction newTransaction = new Transaction();
        newTransaction.setRealizationDate(LocalDateTime.now());
        newTransaction.setOrigin(card.getNumber().toString());
        newTransaction.setDescription("You deposited $" + data.getAmount() + " from " +
                card.getBank() + " " + card.getCardType());
        newTransaction.setDestination(account.getCvu());
        newTransaction.setAmount(data.getAmount());
        newTransaction.setTransactionType(TransactionType.Deposit);
        newTransaction.setAccount(account);

        accountRepository.save(account.updateBalance(data.getAmount()));

        Transaction transaction = transactionRepository.save(newTransaction);

        return transactionMapper.mapToDto(transaction);
    }

    @Override
    public List<TransactionDto> getLastTransactions(Long accountId) {
        List<Transaction> transactions = transactionRepository.findLastFiveByAccountId(accountId);
        List<TransactionDto> transactionsDto = new ArrayList<>();
        for (Transaction t :
                transactions) {
            transactionsDto.add(transactionMapper.mapToDto(t));
        }
        return transactionsDto;
    }

    @Override
    public List<TransactionDto> getTransactions(Long accountId) {
        List<Transaction> transactions = transactionRepository.findAllSorted(accountId);
        List<TransactionDto> transactionsDto = new ArrayList<>();
        for (Transaction t :
                transactions) {
            transactionsDto.add(transactionMapper.mapToDto(t));
        }
        return transactionsDto;
    }

    @Override
    public TransactionDto getTransaction(Long transactionId) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);
        if (transaction.isEmpty()) {
            throw new NotFoundException("Transaction not found : " + transactionId);
        }

        return transactionMapper.mapToDto(transaction.get());
    }
}
