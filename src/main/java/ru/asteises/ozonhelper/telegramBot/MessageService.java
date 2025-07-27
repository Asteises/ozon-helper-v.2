package ru.asteises.ozonhelper.telegramBot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Slf4j
@Service
public class MessageService {

    private final OkHttpTelegramClient client;

    public MessageService(OkHttpTelegramClient client) {
        this.client = client;
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();

        try {
            client.executeAsync(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
