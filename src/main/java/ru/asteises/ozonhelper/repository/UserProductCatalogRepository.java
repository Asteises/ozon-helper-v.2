package ru.asteises.ozonhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.asteises.ozonhelper.model.entities.UserProductCatalogEntity;

import java.util.Optional;

@Repository
public interface UserProductCatalogRepository extends JpaRepository<UserProductCatalogEntity, Long> {

    @Query("""
                SELECT c FROM UserProductCatalogEntity c
                LEFT JOIN FETCH c.products p
                WHERE c.user.telegramUserId = :telegramUserId
            """)
    Optional<UserProductCatalogEntity> findCatalogWithProductsByUserTelegramId(Long telegramUserId);
}
