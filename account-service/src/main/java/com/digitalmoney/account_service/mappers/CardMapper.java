package com.digitalmoney.account_service.mappers;

import com.digitalmoney.account_service.dto.CardDto;
import com.digitalmoney.account_service.entity.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    @Autowired
    public CardMapper(ObjectMapper objectMapper) {
    }

    public CardDto mapToDto(Card data) {
        CardDto dto = new CardDto();
        dto.setId(data.getId());
        dto.setHolder(data.getHolder());
        dto.setNumber(data.getNumber());
        dto.setCardType(data.getCardType());
        dto.setCvv(data.getCvv());
        dto.setExpirationDate(data.getExpirationDate());
        dto.setBank(data.getBank());
        dto.setNetwork(data.getNetwork());
        return dto;
    }


}
