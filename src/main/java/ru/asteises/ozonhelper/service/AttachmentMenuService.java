package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.menubutton.SetChatMenuButton;
import org.telegram.telegrambots.meta.api.objects.menubutton.MenuButtonWebApp;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.asteises.ozonhelper.exeption.TelegramApiCallException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentMenuService {

    private final OkHttpTelegramClient okHttpTelegramClient;

    public void createAndSendWebbAppAttachmentMenu(Long chatId, String webbAppUrl) {
        WebAppInfo webAppInfo = WebAppInfo.builder().url(webbAppUrl).build();
        MenuButtonWebApp menuButtonWebApp = MenuButtonWebApp.builder()
                .text("Перейти в приложение")
                .text("Приложение")
                .webAppInfo(webAppInfo)
                .build();

        SetChatMenuButton setChatMenuButton = SetChatMenuButton.builder()
                .chatId(chatId)
                .menuButton(menuButtonWebApp)
                .build();

        try {
            log.debug("Trying to create menu button for web app: [ {} ]", webAppInfo.getUrl());
            okHttpTelegramClient.execute(setChatMenuButton);
        } catch (TelegramApiException e) {
            log.error("Failed to set menu button for chat: [ {} ]: with message: [ {} ]", chatId, e.getMessage(), e);
            throw new TelegramApiCallException("Failed to set menu button", e);
        }
    }
}
