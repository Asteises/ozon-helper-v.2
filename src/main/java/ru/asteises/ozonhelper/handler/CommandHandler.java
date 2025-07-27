package ru.asteises.ozonhelper.handler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.asteises.ozonhelper.enums.CommandType;
import ru.asteises.ozonhelper.enums.HandlerType;
import ru.asteises.ozonhelper.handler.executor.CommandExecutor;
import ru.asteises.ozonhelper.utils.TelegramUtils;

import java.util.List;
import java.util.Map;

import static ru.asteises.ozonhelper.utils.TelegramUtils.getChatId;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommandHandler implements UpdateHandler {

    private final List<CommandExecutor> executors;
    private final Map<CommandType, CommandExecutor> executorMap;

    @PostConstruct
    public void debugHandlers() {
        if (executors.isEmpty()) {
            log.warn("No command executors found");
        } else {
            log.debug("Found: [ {} ] command executors", executors.size());
            for (CommandExecutor executor : executors) {
                log.debug("Command executor register: [ {} ]", executor.getClass().getSimpleName());
            }
        }
    }

    @Override
    public HandlerType getType() {
        return HandlerType.COMMAND;
    }

    @Override
    public boolean canHandle(Update update) {
        Long chatId = getChatId(update);
        if (update.hasMessage() && update.getMessage().hasText() && update.getMessage().getText().startsWith("/")) {
            String commandText = TelegramUtils.getCommandText(chatId, update, log);
            if (!CommandType.isExist(commandText)) {
                log.warn("Unknown command for text: {}", commandText);
                return false;
            }
        }
        return true;
    }

    @Override
    public void handle(Update update) {
        Long chatId = getChatId(update);
        CommandType command = CommandType.getCommand(TelegramUtils.getCommandText(chatId, update, log)); // TODO Пишем второй раз лог
        CommandExecutor commandExecutor = executorMap.get(command);

        if (commandExecutor != null) {
            log.debug("Find executor for command: [ {} ]", command);
            commandExecutor.execute(chatId);
        } else {
            log.warn("No command executor found for type: [ {} ] for update: [ {} ]", command, update.getUpdateId());
        }
    }
}
