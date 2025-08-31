package com.example.demo.service;

import com.example.demo.api.model.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public ResponseEntity<ErrorDto> handleException(HttpStatus httpStatus, Exception exception) {
        var errorDto = new ErrorDto();
        errorDto.setDescription(exception.getMessage());
        return ResponseEntity.status(httpStatus)
                             .body(errorDto);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(Exception exception) {
        return handleException(HttpStatus.BAD_REQUEST, exception);
    }
}
