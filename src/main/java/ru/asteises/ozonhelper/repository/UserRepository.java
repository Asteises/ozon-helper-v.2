package ru.asteises.ozonhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.asteises.ozonhelper.model.entities.UserEntity;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.telegramUserId = :telegramUserId")
    Optional<UserEntity> findByTelegramUserId(@Param("telegramUserId") Long telegramUserId);
}
