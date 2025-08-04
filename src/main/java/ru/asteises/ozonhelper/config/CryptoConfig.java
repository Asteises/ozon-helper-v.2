package ru.asteises.ozonhelper.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
public class CryptoConfig {

    @Bean
    public TextEncryptor textEncryptor(@Value("${encryption.password}") String password,
                                       @Value("${encryption.salt}") String salt) {
        return Encryptors.text(password, salt);
    }
}
