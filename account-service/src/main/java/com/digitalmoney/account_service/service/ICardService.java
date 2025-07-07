package com.digitalmoney.account_service.service;

import com.digitalmoney.account_service.controller.requestDto.CardCreateDTO;
import com.digitalmoney.account_service.controller.requestDto.CardUpdateDTO;
import com.digitalmoney.account_service.dto.CardDto;

import java.util.List;

public interface ICardService {
    CardDto createCard(CardCreateDTO data) throws Exception;

    CardDto updateCard(Long accountId, CardUpdateDTO data) throws Exception;

    CardDto findByIdAndAccountId(Long cardId, Long accountId) throws Exception;

    List<CardDto> getCards(Long accountId);

    void deleteCard(Long cardId, Long accountId) throws Exception;
}
