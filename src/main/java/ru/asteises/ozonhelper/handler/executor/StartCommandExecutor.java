package ru.asteises.ozonhelper.handler.executor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.asteises.ozonhelper.enums.CommandType;
import ru.asteises.ozonhelper.service.MessageService;
import ru.asteises.ozonhelper.utils.TelegramUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartCommandExecutor implements CommandExecutor {

    @Value("${telegram.miniapp.url}")
    private String miniAppUrl;

    private final MessageService messageService;

    @Override
    public CommandType getCommandType() {
        return CommandType.START;
    }

    @Override
    public void execute(Long chatId) {
        log.info("Executing start command");
        InlineKeyboardMarkup markup = TelegramUtils.getWebAppSingleButton("Начать работу", miniAppUrl);
//        messageService.sendInlineButtonMessage(chatId, "Добро пожаловать! Используйте кнопки для работы.", markup); // FIXME Ссылки должны быть https
        messageService.sendMessage(chatId, "Добро пожаловать! Используйте кнопки для работы.");
    }
}
