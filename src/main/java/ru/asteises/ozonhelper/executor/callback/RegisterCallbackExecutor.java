package ru.asteises.ozonhelper.executor.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.asteises.ozonhelper.enums.CallbackType;
import ru.asteises.ozonhelper.model.ButtonDto;
import ru.asteises.ozonhelper.model.UserEntity;
import ru.asteises.ozonhelper.service.MessageService;
import ru.asteises.ozonhelper.service.UserService;
import ru.asteises.ozonhelper.utils.KeyboardUtils;
import ru.asteises.ozonhelper.utils.TelegramUtils;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterCallbackExecutor implements CallbackExecutor {

    private final UserService userService;
    private final MessageService messageService;

    @Override
    public CallbackType getType() {
        return CallbackType.REGISTER;
    }

    @Override
    public void execute(Update update) {
        User user = update.getCallbackQuery().getFrom();
        Long userTgId = user.getId();
    }

    private void sendEmptyMessage(Update update) {
        Long chatId = TelegramUtils.getChatId(update.getCallbackQuery());
        messageService.sendMessage(chatId, "Поздравляем, вы успешно добавили все данные. Начните работу с нажатия кнопки:");
    }

    private void sendMessage(Update update) {
        Long chatId = TelegramUtils.getChatId(update.getCallbackQuery());
        ButtonDto clientIdButton = KeyboardUtils.getButton(CallbackType.SENT_OZON_CLIENT_ID.getName(), CallbackType.SENT_OZON_CLIENT_ID);
        ButtonDto apiKeyButton = KeyboardUtils.getButton(CallbackType.SENT_OZON_SELLER_API_KEY.getName(), CallbackType.SENT_OZON_SELLER_API_KEY);

        InlineKeyboardMarkup replyKeyboardMarkup = KeyboardUtils.getMultiRowInlineKeyboard(List.of(Collections.singletonList(clientIdButton), Collections.singletonList(apiKeyButton)));
        messageService.sendMessage(chatId, "Поздравляем, вы успешно зарегистрировались! Для начала работы, нужно добавить ваши данные из Ozon.", replyKeyboardMarkup);
    }


}
