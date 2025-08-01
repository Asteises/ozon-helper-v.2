package ru.asteises.ozonhelper.mapper;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.asteises.ozonhelper.enums.UserRole;
import ru.asteises.ozonhelper.enums.UserStatus;
import ru.asteises.ozonhelper.model.UserDto;
import ru.asteises.ozonhelper.model.UserEntity;

import java.time.LocalDateTime;

public class Mapper {

    public static UserEntity mapUser(User user) {
        return UserEntity.builder()
                .telegramUserId(user.getId())
                .role(UserRole.SELLER)
                .status(UserStatus.ACTIVE)
                .username(user.getUserName())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .registeredAt(LocalDateTime.now())
                .build();
    }

    public static UserDto mapUser(UserEntity user) {
        return UserDto.builder()
                .build();
    }
}
