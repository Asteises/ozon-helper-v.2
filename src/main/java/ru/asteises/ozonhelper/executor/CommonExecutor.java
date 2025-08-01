package ru.asteises.ozonhelper.executor;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommonExecutor<T> {

    T getType();

    void execute(Update update);
}
