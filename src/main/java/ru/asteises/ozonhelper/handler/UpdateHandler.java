package ru.asteises.ozonhelper.handler;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.asteises.ozonhelper.enums.HandlerType;

public interface UpdateHandler {

    HandlerType getType();

    boolean canHandle(Update update);

    void handle(Update update);
}
