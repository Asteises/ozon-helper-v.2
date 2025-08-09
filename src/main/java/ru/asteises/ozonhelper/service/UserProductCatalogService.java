package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asteises.ozonhelper.mapper.Mapper;
import ru.asteises.ozonhelper.model.entities.UserEntity;
import ru.asteises.ozonhelper.model.entities.UserProductCatalogEntity;
import ru.asteises.ozonhelper.repository.UserProductCatalogRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProductCatalogService {

    private final UserProductCatalogRepository repository;

    @Transactional
    public void save(UserProductCatalogEntity catalogEntity) {
        log.info("Save catalog for user id: [ {} ]", catalogEntity.getId());
        repository.save(catalogEntity);
    }

    public Optional<UserProductCatalogEntity> getByTelegramUserId(Long telegramUserId) {
        return repository.findCatalogWithProductsByUserTelegramId(telegramUserId);
    }

    @Transactional
    public Optional<UserProductCatalogEntity> getByUserId(UserEntity userEntity) {
        return repository.findCatalogWithProductsByUserTelegramId(userEntity.getTelegramUserId());
    }

    @Transactional
    public UserProductCatalogEntity getOrCreateCatalog(UserEntity userEntity) {
        Long userTgId = userEntity.getTelegramUserId();
        log.info("Get or create catalog for user id: [ {} ]", userTgId);
        Optional<UserProductCatalogEntity> optionalEntity = getByTelegramUserId(userTgId);
        // Если каталог для пользователя уже существует
        if (optionalEntity.isPresent()) {
            log.info("User catalog already exists with id: [ {} ]", userTgId);
            return optionalEntity.get();
        }
        // Иначе, создаем новый каталог
        log.info("User catalog created for user id: [ {} ]", userTgId);
        UserProductCatalogEntity catalogEntity = Mapper.initMapUserProductCatalog(userEntity);
        return repository.save(catalogEntity);
    }
}
