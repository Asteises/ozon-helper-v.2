package ru.asteises.ozonhelper.model;

import lombok.*;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestData {

    private Long telegramUserId;

    private String taskId;
}
