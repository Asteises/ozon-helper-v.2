package ru.asteises.ozonhelper.model;

import lombok.*;
import ru.asteises.ozonhelper.enums.UserRole;
import ru.asteises.ozonhelper.enums.UserStatus;

import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private UserRole role;

    private UserStatus status;

    private Long telegramUserId;

    private String username;

    private String firstName;

    private String lastName;

    private LocalDateTime registeredAt;
}
