package ru.asteises.ozonhelper.handler.executor;

import ru.asteises.ozonhelper.enums.CommandType;

public interface CommandExecutor {

    CommandType getCommandType();

    void execute(Long chatId);
}
