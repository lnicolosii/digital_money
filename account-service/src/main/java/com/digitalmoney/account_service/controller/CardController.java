package com.digitalmoney.account_service.controller;

import com.digitalmoney.account_service.controller.requestDto.CardCreateDTO;
import com.digitalmoney.account_service.controller.requestDto.CardUpdateDTO;
import com.digitalmoney.account_service.service.impl.CardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/cards")
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<?> createCard(@RequestBody CardCreateDTO data) {
        log.info("Card creation request");
        return ResponseEntity.status(HttpStatus.CREATED).body(cardService.createCard(data));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getCards(@PathVariable Long accountId) {
        return ResponseEntity.ok(cardService.getCards(accountId));
    }

    @PatchMapping("/{cardId}")
    public ResponseEntity<?> updateCard(@PathVariable Long cardId, @RequestBody CardUpdateDTO data) throws Exception {
        log.info("Card update request");
        return ResponseEntity.ok(cardService.updateCard(cardId, data));
    }

    @GetMapping("/{cardId}/account/{accountId}")
    public ResponseEntity<?> getCardByIdAndAccountId(@PathVariable Long cardId, @PathVariable Long accountId) throws Exception {
        log.info("Get card by account ID request: {}", accountId);
        return ResponseEntity.ok(cardService.findByIdAndAccountId(cardId, accountId));
    }

    @DeleteMapping("/{cardId}/account/{accountId}")
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId, @PathVariable Long accountId) throws Exception {
        log.info("Delete card request - cardId: {}, accountId: {}", cardId, accountId);
        cardService.deleteCard(cardId, accountId);
        return ResponseEntity.ok().build();
    }
}
