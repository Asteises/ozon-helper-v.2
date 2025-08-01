package ru.asteises.ozonhelper.executor.callback;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.asteises.ozonhelper.enums.CallbackType;
import ru.asteises.ozonhelper.service.MessageService;
import ru.asteises.ozonhelper.utils.TelegramUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class OzonSellerApiKeyExecutor implements CallbackExecutor {

    private final MessageService messageService;

    @Override
    public CallbackType getType() {
        return CallbackType.SENT_OZON_SELLER_API_KEY;
    }

    @Override
    public void execute(Update update) {
        messageService.sendMessage(TelegramUtils.getChatId(update.getCallbackQuery()), "Пожалуйста, введите свой Seller API Key");
    }
}
