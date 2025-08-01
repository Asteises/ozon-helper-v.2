package ru.asteises.ozonhelper.utils;

import org.slf4j.Logger;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public class TelegramUtils {

    // TODO Обернуть в TRY CATCH
    public static Long getChatId(Message message) {
        return message.getChatId();
    }

    public static Long getChatId(CallbackQuery callbackQuery) {
        return callbackQuery.getMessage().getChat().getId();
    }

    public static String getCommandText(Long chatId, Update update, Logger log) {
        String commandText = update.getMessage().getText().substring(1);
        log.debug("Update Command Text: [ {} ] for chat: [ {} ]", commandText, chatId);
        return commandText;
    }

    public static String getCommandText(Update update) {
        return update.getMessage().getText().substring(1);
    }

    public static String getCallbackText(Long chatId, Update update, Logger log) {
        String callbackText = update.getCallbackQuery().getData();
        log.debug("Update Callback Text: [ {} ] for chat: [ {} ]", callbackText, chatId);
        return callbackText;
    }

    public static String getCallbackText(Update update) {
        return update.getCallbackQuery().getData();
    }

    public static String getMessageText(Long chatId, Update update, Logger log) {
        String commandText = update.getMessage().getText();
        log.debug("Update Message Text: [ {} ] for chat: [ {} ]", commandText, chatId);
        return commandText;
    }

    public static String getMessageText(Update update) {
        return update.getMessage().getText();
    }
}
