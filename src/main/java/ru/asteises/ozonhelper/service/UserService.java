package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asteises.ozonhelper.mapper.Mapper;
import ru.asteises.ozonhelper.model.RegisterUserData;
import ru.asteises.ozonhelper.model.entities.UserEntity;
import ru.asteises.ozonhelper.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CryptoService cryptoService;

    @Transactional
    public String saveOrUpdateUser(RegisterUserData userData) {
        Long userTgId = userData.getTelegramUserId();
        try {
            UserEntity existingUser = getUserOrNull(userTgId);
            if (existingUser == null) {
                saveNewUser(userData, userTgId);
                return "Поздравляем! Вы зарегистрированы.";
            }
            if (!existingUser.hasAdditionalData()) {
                updateExistUser(userData, userTgId, existingUser);
            }
            return "Ваши данные успешно обновлены.";
        } catch (Exception e) {
            log.error("Something went wrong in user registration process: [ {} ]", e.getMessage(), e);
            return "Что-то пошло не так во время регистрации нового пользователя. Пожалуйста попробуйте позже или обратитесь к администратору.";
        }
    }

    private void updateExistUser(RegisterUserData userData, Long userTgId, UserEntity existingUser) {
        log.debug("Updating data for existing user with td id: [ {} ]", userTgId);
        Mapper.updateUser(existingUser, userData);
        userRepository.save(existingUser);
    }

    private void saveNewUser(RegisterUserData userData, Long userTgId) {
        log.debug("Registering new user with tg id: [ {} ]", userTgId);
        UserEntity newUser = Mapper.mapUser(userData);
        encryptSecrets(userData, newUser);
        userRepository.save(newUser);
    }

    private void encryptSecrets(RegisterUserData userData, UserEntity existingUser) {
        if (userData.getOzonDataForm() != null) {
            existingUser.setClientId(userData.getOzonDataForm().getClientId());
            String encryptApiKey = cryptoService.encrypt(userData.getOzonDataForm().getApiKey());
            existingUser.setEncryptedApiKey(encryptApiKey);
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
