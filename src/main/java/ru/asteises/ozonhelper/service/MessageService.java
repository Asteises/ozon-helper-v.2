package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final OkHttpTelegramClient client;

    public void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = getMessage(chatId, message);
        executeMessage(sendMessage);
    }

    public void sendInlineButtonMessage(Long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(inlineKeyboardMarkup)
                .build();
        log.debug("Send message {}", sendMessage);
        executeMessage(sendMessage);
    }

    public SendMessage getMessage(Long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .build();
        log.debug("Create SendMessage: [ {} ] in chat: [ {} ]", sendMessage, chatId);
        return sendMessage;
    }

    private void executeMessage(SendMessage sendMessage) {
        try {
            log.debug("Payload to Telegram: {}", sendMessage);
            client.executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
