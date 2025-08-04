package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asteises.ozonhelper.mapper.Mapper;
import ru.asteises.ozonhelper.model.RegisterUserData;
import ru.asteises.ozonhelper.model.UserEntity;
import ru.asteises.ozonhelper.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public String saveUser(RegisterUserData userData) {
        Long userTgId = userData.getTelegramUserId();
        try {
            UserEntity userEntity = getUserOrNull(userTgId);
            // Пользователь уже зарегистрирован
            if (userEntity != null) {
                log.debug("User tg id: [ {} ] already exists", userTgId);
                if (!userEntity.hasSecrets()) {
                    log.debug("User tg id: [ {} ] already exists but don't has secrets", userTgId);
                } else {
                    return "Вы уже зарегистрированы. Повторная регистрация не требуется.";
                }
            }
            log.debug("User tg id: [ {} ] register", userTgId);
            userEntity = Mapper.mapUser(userData);
            userRepository.save(userEntity);
            return "Поздравляем! Вы зарегистрированы.";
        } catch (Exception e) {
            log.error("Something went wrong in user registration process: [ {} ]", e.getMessage(), e);
            return "Что-то пошло не так во время регистрации нового пользователя. Пожалуйста попробуйте позже или обратитесь к администратору.";
        }
    }

    public Optional<UserEntity> getOptionalUser(Long tgUserId) {
        return userRepository.findByTelegramUserId(tgUserId);
    }

    public UserEntity getUserOrNull(Long tgUserId) {
        Optional<UserEntity> oUserEntity = getOptionalUser(tgUserId);
        if (oUserEntity.isPresent()) {
            UserEntity userEntity = oUserEntity.get();
            log.debug("Found UserEntity id: [ {} ] for user tg id: [ {} ]", userEntity.getId(), tgUserId);
            return userEntity;
        }
        log.debug("Can't found UserEntity for user tg id: [ {} ]", tgUserId);
        return null;
    }
}
