package ru.asteises.ozonhelper.model;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSecretDto {

    private String clientId;

    private String encryptedApiKey;
}
