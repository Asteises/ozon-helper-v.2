package ru.asteises.ozonhelper.config.telegram;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Configuration
public class BotConfig {

    @Value("${telegram.bot.token}")
    private String BOT_TOKEN;

    @Bean
    public TelegramBotsLongPollingApplication telegramBotsApplication(LongPollingUpdateConsumer updateConsumer) {
        TelegramBotsLongPollingApplication app = new TelegramBotsLongPollingApplication();
        try {
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
    public LongPollingUpdateConsumer updateConsumer() {
        return (List<Update> updates) -> {
            // Логика обработки
            log.debug("Update received: {}", updates);
        };
    }
}
