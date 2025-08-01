package ru.asteises.ozonhelper.executor.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.asteises.ozonhelper.enums.CommandType;
import ru.asteises.ozonhelper.service.MessageService;
import ru.asteises.ozonhelper.utils.KeyboardUtils;

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
        String webAppFullUrl = String.format("%s/miniapp", miniAppUrl);
        log.info("Miniapp URL: {}", webAppFullUrl);
        InlineKeyboardMarkup markup = KeyboardUtils.getSimpleInlineKeyboard("Зарегистрироваться", webAppFullUrl);
        messageService.sendMessage(update.getMessage().getChatId(), "Начните работу с нажатия кнопки:", markup);
    }
}
