package ru.asteises.ozonhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.asteises.ozonhelper.model.entities.ProductEntity;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    // Все продукты для каталога с квантами (одним запросом)
    @Query("""
                SELECT p FROM ProductEntity p
                LEFT JOIN FETCH p.quants
                WHERE p.catalog.id = :catalogId
            """)


    List<ProductEntity> findByCatalogIdWithQuants(Long catalogId);

}
