package ru.asteises.ozonhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asteises.ozonhelper.model.entities.ProductQuantEntity;

@Repository
public interface ProductQuantRepository extends JpaRepository<ProductQuantEntity, Long> {
}
