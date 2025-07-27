package ru.asteises.ozonhelper.listener;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.asteises.ozonhelper.enums.HandlerType;
import ru.asteises.ozonhelper.handler.UpdateHandler;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateListener implements LongPollingUpdateConsumer {

    private final List<UpdateHandler> updateHandlers;
    private final Map<HandlerType, UpdateHandler> handlerMap;

    @PostConstruct
    public void debugHandlers() {
        if (updateHandlers.isEmpty()) {
            log.warn("No updateHandlers found");
        } else {
            log.debug("Found: [ {} ] updateHandlers", updateHandlers.size());
            for (UpdateHandler updateHandler : updateHandlers) {
                log.debug("Handler register: [ {} ]", updateHandler.getClass().getSimpleName());
            }
        }
    }

    @Override
    public void consume(List<Update> list) {
        log.debug("Update list: {}", list);
        for (Update update : list) {
            log.debug("Income Update: [ {} ]", update);

            HandlerType type = detectHandlerType(update);
            UpdateHandler handler = handlerMap.get(type);

            if (handler != null && handler.canHandle(update)) {
                handler.handle(update);
            } else {
                log.warn("No handler found for type: [ {} ] for update: [ {} ]", type, update.getUpdateId());
            }
        }
    }

    private HandlerType detectHandlerType(Update update) {
        if (update.hasCallbackQuery()) return HandlerType.CALLBACK;
        if (update.hasMessage() && update.getMessage().getWebAppData() != null) return HandlerType.WEBAPP;
        if (update.hasMessage() && update.getMessage().getText() != null && update.getMessage().getText().startsWith("/"))
            return HandlerType.COMMAND;
        return HandlerType.MESSAGE;
    }
}
