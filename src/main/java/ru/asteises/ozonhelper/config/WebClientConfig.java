package ru.asteises.ozonhelper.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        // Раздаём все файлы из static/miniapp/
//        registry.addResourceHandler("/miniapp/**")
//                .addResourceLocations("classpath:/static/miniapp/");
//    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Все пути без точки (т.е. не файлы) перенаправляем на index.html
        registry.addViewController("/miniapp/{path:^(?!.*\\.).*$}")
                .setViewName("forward:/static/miniapp/index.html");

        // Чтобы работал и с хвостовым слэшем
        registry.addViewController("/miniapp/")
                .setViewName("forward:/static/miniapp/index.html");
    }
}
