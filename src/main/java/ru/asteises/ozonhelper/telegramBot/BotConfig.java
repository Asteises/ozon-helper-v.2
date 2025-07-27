package ru.asteises.ozonhelper.telegramBot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.asteises.ozonhelper.enums.CommandType;
import ru.asteises.ozonhelper.enums.HandlerType;
import ru.asteises.ozonhelper.handler.UpdateHandler;
import ru.asteises.ozonhelper.handler.executor.CommandExecutor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class BotConfig {

    @Value("${telegram.bot.token}")
    private String BOT_TOKEN;

    @Bean
    public TelegramBotsLongPollingApplication telegramBotsApplication(LongPollingUpdateConsumer updateConsumer) {
        TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();
        try {
            log.debug("Start Telegram Bot -->");
            app.registerBot(BOT_TOKEN, updateConsumer);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        return app;
    }

    @Bean
    public OkHttpTelegramClient telegramClient() {
        return new OkHttpTelegramClient(BOT_TOKEN);
    }

    @Bean
    public Map<HandlerType, UpdateHandler> handlerHandlers(List<UpdateHandler> handlers) {
        return handlers.stream()
                .collect(Collectors.toMap(UpdateHandler::getType, uH -> uH));
    }

    @Bean
    public Map<CommandType, CommandExecutor> handleExecutors(List<CommandExecutor> executors) {
        return executors.stream()
                .collect(Collectors.toMap(CommandExecutor::getCommandType, cE -> cE));
    }
}
