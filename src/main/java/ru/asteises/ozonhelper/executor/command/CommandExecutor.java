package ru.asteises.ozonhelper.executor.command;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.asteises.ozonhelper.enums.CommandType;
import ru.asteises.ozonhelper.executor.CommonExecutor;

public interface CommandExecutor extends CommonExecutor<CommandType> {

    CommandType getType();

    void execute(Update update);
}
