package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    public void save(UserProductCatalogEntity catalogEntity) {
        log.info("Save catalog for user id: [ {} ]", catalogEntity.getId());
        repository.save(catalogEntity);
    }

    public Optional<UserProductCatalogEntity> getByUserId(UserEntity userEntity) {
        return repository.findByUserId(userEntity.getId());
    }

    public Optional<UserProductCatalogEntity> getByTelegramUserId(Long telegramUserId) {
        return repository.findByUser_TelegramUserId(telegramUserId);
    }

    public UserProductCatalogEntity getOrCreateCatalog(UserEntity userEntity) {
        log.info("Get or create catalog for user id: [ {} ]", userEntity.getId());
        Optional<UserProductCatalogEntity> optionalEntity = getByUserId(userEntity);
        if (optionalEntity.isPresent()) {
            log.info("User catalog already exists with id: [ {} ]", userEntity.getId());
            return optionalEntity.get();
        }
        log.info("User catalog created for user id: [ {} ]", userEntity.getId());
        UserProductCatalogEntity catalogEntity = Mapper.mapUserProductCatalog(userEntity);
        return repository.save(catalogEntity);
    }
}
