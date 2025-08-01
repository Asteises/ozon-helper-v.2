package ru.asteises.ozonhelper.handler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.asteises.ozonhelper.enums.CallbackType;
import ru.asteises.ozonhelper.enums.HandlerType;
import ru.asteises.ozonhelper.executor.callback.CallbackExecutor;
import ru.asteises.ozonhelper.utils.TelegramUtils;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class CallbackHandler implements UpdateHandler {

    private final List<CallbackExecutor> executors;
    private final Map<CallbackType, CallbackExecutor> executorMap;

    @PostConstruct
    public void debugExecutors() {
        if (executors.isEmpty()) {
            log.warn("No callback executors found");
        } else {
            log.debug("Found: [ {} ] callback executors", executors.size());
            for (CallbackExecutor executor : executors) {
                log.debug("Callback executor register: [ {} ]", executor.getClass().getSimpleName());
            }
        }
    }

    @Override
    public HandlerType getType() {
        return HandlerType.CALLBACK;
    }

    @Override
    public boolean canHandle(Update update) {
        if (update.hasCallbackQuery() && update.getCallbackQuery().getData() != null) {
            Long chatId = TelegramUtils.getChatId(update.getCallbackQuery());
            String callbackText = TelegramUtils.getCallbackText(chatId, update, log);
            if (!CallbackType.isExist(callbackText)) {
                log.warn("Unknown callback for text: {}", callbackText);
                return false;
            }
        }
        return true;
    }

    @Override
    public void handle(Update update) {
        CallbackType callbackType = CallbackType.getCallback(TelegramUtils.getCallbackText(update));
        CallbackExecutor callbackExecutor = executorMap.get(callbackType);

        if (callbackExecutor != null) {
            log.debug("Find executor for callback: [ {} ]", callbackType);
            callbackExecutor.execute(update);
        } else {
            log.warn("No callback executor found for type: [ {} ] for update: [ {} ]", callbackType, update.getUpdateId());
        }
    }
}
