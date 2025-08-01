package ru.asteises.ozonhelper.executor.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.asteises.ozonhelper.enums.CallbackType;
import ru.asteises.ozonhelper.enums.CommandType;
import ru.asteises.ozonhelper.model.ButtonDto;
import ru.asteises.ozonhelper.service.MessageService;
import ru.asteises.ozonhelper.utils.KeyboardUtils;
import ru.asteises.ozonhelper.utils.TelegramUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommandExecutor implements CommandExecutor {

    @Value("${telegram.miniapp.url}")
    private String miniAppUrl;

    private final MessageService messageService;

    @Override
    public CommandType getType() {
        return CommandType.START;
    }

    @Override
    public void execute(Update update) {
        log.info("Executing start command");

        InlineKeyboardMarkup markup = KeyboardUtils.getSimpleInlineKeyboard("Зарегистрироваться", CallbackType.REGISTER);
        messageService.sendMessage(update.getMessage().getChatId(), "Начните работу с нажатия кнопки:", markup);
    }
}
