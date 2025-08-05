package ru.asteises.ozonhelper.model;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckUserData {

    private Long telegramUserId;

    private String telegramInitData;
}
