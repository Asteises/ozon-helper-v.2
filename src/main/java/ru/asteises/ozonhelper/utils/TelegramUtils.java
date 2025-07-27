package ru.asteises.ozonhelper.utils;

import org.slf4j.Logger;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.api.objects.webapp.WebAppInfo;

public class TelegramUtils {

    public static Long getChatId(Update update) {
        return update.getMessage().getChatId();
    }

    public static InlineKeyboardMarkup getWebAppSingleButton(String text, String miniAppUrl) {
        InlineKeyboardButton startButton = InlineKeyboardButton.builder()
                .text(text)
                .webApp(new WebAppInfo(miniAppUrl))
                .build();

        return InlineKeyboardMarkup.builder()
                .keyboardRow(new InlineKeyboardRow(startButton))
                .build();
    }

    public static String getCommandText(Long chatId, Update update, Logger log) {
        String commandText = update.getMessage().getText().substring(1);
        log.debug("Update Command Text: [ {} ] for chat: [ {} ]", commandText, chatId);
        return commandText;
    }
}
