package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;
import ru.asteises.ozonhelper.mapper.Mapper;
import ru.asteises.ozonhelper.model.UserEntity;
import ru.asteises.ozonhelper.repository.UserRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void saveUser(User user) {
        UserEntity userEntity = Mapper.mapUser(user);
        userEntity = userRepository.save(userEntity);
        log.debug("Saved user id: [ {} ] for TG User id: [ {} ]", userEntity.getId(), userEntity.getTelegramUserId());
    }

    public Optional<UserEntity> getOptionalUser(User user) {
        Long tgUserId = getUserTgId(user);
        return userRepository.findByTelegramUserId(tgUserId);
    }

    public UserEntity getUser(User user) {
        Long tgUserId = getUserTgId(user);
        Optional<UserEntity>  oUserEntity = getOptionalUser(user);
        if (oUserEntity.isPresent()) {
            UserEntity userEntity = oUserEntity.get();
            log.debug("Found UserEntity id: [ {} ] for user tg id: [ {} ]", userEntity.getId(), tgUserId);
            return userEntity;
        }
        log.debug("Can't found UserEntity for user tg id: [ {} ]", tgUserId);
        throw new RuntimeException(String.format("Can't find UserEntity for user tg id: [ %s ]", tgUserId));
    }

    private Long getUserTgId(User user) {
        return user.getId();
    }
}
