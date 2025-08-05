package ru.asteises.ozonhelper.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebClientConfig implements WebMvcConfigurer {

    @Value("${ozon.api.url}")
    private String baseUrl;

    @Bean
    public WebClient ozonWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/dev/bot/ozon/helper")
                .setViewName("forward:/miniapp/index.html");

        registry.addViewController("/dev/bot/ozon/helper/{path:[^\\.]*}")
                .setViewName("forward:/miniapp/index.html");
    }
}
