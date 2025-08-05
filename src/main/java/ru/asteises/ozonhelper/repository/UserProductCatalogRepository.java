package ru.asteises.ozonhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.asteises.ozonhelper.model.entities.UserProductCatalogEntity;

import java.util.Optional;

@Repository
public interface UserProductCatalogRepository extends JpaRepository<UserProductCatalogEntity, Long> {

    // Загрузить каталог с продуктами и квантами (двойной fetch join)
    @Query("""
                SELECT c FROM UserProductCatalogEntity c
                LEFT JOIN FETCH c.products p
                LEFT JOIN FETCH p.quants
                WHERE c.user.id = :userId
            """)
    Optional<UserProductCatalogEntity> findByUserIdWithProductsAndQuants(Long userId);

    // Быстрая проверка наличия каталога
    Optional<UserProductCatalogEntity> findByUserId(Long userId);

    Optional<UserProductCatalogEntity> findByUser_TelegramUserId(Long telegramUserId);
}
