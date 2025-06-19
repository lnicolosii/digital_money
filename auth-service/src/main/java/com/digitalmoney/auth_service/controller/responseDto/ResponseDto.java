package com.digitalmoney.auth_service.controller.responseDto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseDto<T> {
    private String message;
    private LocalDateTime createdAt;
    private T data;

    public ResponseDto(String message, LocalDateTime createdAt, T data) {
        this.message = message;
        this.createdAt = createdAt;
        this.data = data;
    }
}
