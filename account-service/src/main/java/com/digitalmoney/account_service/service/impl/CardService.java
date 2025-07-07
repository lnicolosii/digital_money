package com.digitalmoney.account_service.service.impl;

import com.digitalmoney.account_service.controller.requestDto.CardCreateDTO;
import com.digitalmoney.account_service.controller.requestDto.CardUpdateDTO;
import com.digitalmoney.account_service.dto.CardDto;
import com.digitalmoney.account_service.entity.Account;
import com.digitalmoney.account_service.entity.Card;
import com.digitalmoney.account_service.exceptions.BadRequestException;
import com.digitalmoney.account_service.exceptions.NotFoundException;
import com.digitalmoney.account_service.mappers.CardMapper;
import com.digitalmoney.account_service.repository.IAccountRepository;
import com.digitalmoney.account_service.repository.ICardRepository;
import com.digitalmoney.account_service.service.ICardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CardService implements ICardService {
    private IAccountRepository accountRepository;
    private ICardRepository cardRepository;
    private CardMapper mapper;

    @Autowired
    public CardService(IAccountRepository accountRepository, ICardRepository cardRepository, CardMapper mapper) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public CardDto createCard(CardCreateDTO data) {
        if (checkIfCardExists(data.getNumber())) {
            throw new BadRequestException("The card you are trying to create already exists");
        }
        Card card = new Card();
        card.setNumber(data.getNumber());
        card.setHolder(data.getHolder());
        card.setBank(data.getBank());
        card.setCardType(data.getCardType());
        card.setExpirationDate(data.getExpirationDate());
        card.setCvv(data.getCvv());

        Optional<Account> account = accountRepository.findById(data.getAccountId());
        if (account.isEmpty()) {
            throw new NotFoundException("Account not found");
        }
        card.setAccount(account.get());
        cardRepository.save(card);
        return mapper.mapToDto(card);
    }

    @Transactional
    @Override
    public CardDto updateCard(Long cardId, CardUpdateDTO data) throws Exception {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public CardDto findByIdAndAccountId(Long cardId, Long accountId) throws Exception {
        Optional<Card> cardResult = cardRepository.findByIdAndAccountId(cardId, accountId);
        if (cardResult.isEmpty()) {
            throw new NotFoundException("Card not found");
        }
        return mapper.mapToDto(cardResult.get());
    }


    @Transactional(readOnly = true)
    @Override
    public List<CardDto> getCards(Long accountId) {
        List<Card> cards = cardRepository.findAllByAccountId(accountId);
        List<CardDto> cardsDto = new ArrayList<>();
        for (Card card : cards) {
            cardsDto.add(mapper.mapToDto(card));
        }
        return cardsDto;
    }

    @Transactional
    @Override
    public void deleteCard(Long cardId, Long accountId) throws Exception {
        Optional<Card> cardResult = cardRepository.findByIdAndAccountId(cardId, accountId);
        if (cardResult.isEmpty()) {
            throw new NotFoundException("Card not found");
        }
        cardRepository.delete(cardResult.get());
    }

    //utils
    private boolean checkIfCardExists(Long cardNumber) {
        return cardRepository.findByNumber(cardNumber).isPresent();
    }
}
