package ru.asteises.ozonhelper.exeption;

public class TelegramApiCallException extends RuntimeException {
    public TelegramApiCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
