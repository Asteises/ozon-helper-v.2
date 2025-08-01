package ru.asteises.ozonhelper.executor.callback;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.asteises.ozonhelper.enums.CallbackType;
import ru.asteises.ozonhelper.executor.CommonExecutor;

public interface CallbackExecutor extends CommonExecutor<CallbackType> {

    CallbackType getType();

    void execute(Update update);
}
