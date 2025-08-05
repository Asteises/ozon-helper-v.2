package ru.asteises.ozonhelper.exeption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TelegramApiCallException.class)
    public ResponseEntity<String> handleTelegramApiCallException(final TelegramApiCallException exception) {
        log.error("Telegram API error: [ {} ]", exception.getMessage(), exception);
        return ResponseEntity.ok("Error handled: " + exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.ok("Unexpected error occurred");
    }
}
