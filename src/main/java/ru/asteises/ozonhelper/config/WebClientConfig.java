package ru.asteises.ozonhelper.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
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
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/miniapp/**")
                .addResourceLocations("/resources/", "resources/static/miniapp/", "classpath:/static/miniapp/")
                .setCachePeriod(0);
    }
}
