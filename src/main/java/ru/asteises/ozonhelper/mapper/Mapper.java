package ru.asteises.ozonhelper.mapper;

import org.telegram.telegrambots.meta.api.objects.User;
import ru.asteises.ozonhelper.enums.UserRole;
import ru.asteises.ozonhelper.enums.UserStatus;
import ru.asteises.ozonhelper.model.RegisterUserData;
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

    public static UserEntity mapUser(RegisterUserData registerUserData) {
        return UserEntity.builder()
                .telegramUserId(registerUserData.getTelegramUserId())
                .clientId(registerUserData.getOzonDataForm().getClientId())
                .encryptedApiKey(registerUserData.getOzonDataForm().getApiKey())
                .role(UserRole.SELLER)
                .status(UserStatus.ACTIVE)
                .username(registerUserData.getUsername())
                .firstName(registerUserData.getFirstName())
                .lastName(registerUserData.getLastName())
                .registeredAt(LocalDateTime.now())
                .build();
    }

    public static void updateUser(UserEntity userEntity, RegisterUserData registerUserData) {
        if (registerUserData.getUsername() != null) userEntity.setUsername(registerUserData.getUsername());
        if (registerUserData.getFirstName() != null) userEntity.setFirstName(registerUserData.getFirstName());
        if (registerUserData.getLastName() != null) userEntity.setLastName(registerUserData.getLastName());
    }

    public static UserDto mapUser(UserEntity user) {
        return UserDto.builder()
                .build();
    }
}
