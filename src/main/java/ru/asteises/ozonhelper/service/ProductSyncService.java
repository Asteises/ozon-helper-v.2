package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asteises.ozonhelper.enums.CatalogStatus;
import ru.asteises.ozonhelper.mapper.Mapper;
import ru.asteises.ozonhelper.model.entities.ProductEntity;
import ru.asteises.ozonhelper.model.entities.UserEntity;
import ru.asteises.ozonhelper.model.entities.UserProductCatalogEntity;
import ru.asteises.ozonhelper.model.ozon.ProductListResponse;
import ru.asteises.ozonhelper.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSyncService {

    private final OzonProductService ozonProductService;
    private final UserProductCatalogService userProductCatalogService;
    private final ProductRepository productRepository;

    @Async("taskExecutor")
    @Transactional
    public void syncUserProductsAsync(UserEntity userEntity) {
        syncUserProducts(userEntity);
    }

    public void syncUserProducts(UserEntity userEntity) {
        log.debug("Синхронизация товаров для пользователя: [ {} ]", userEntity.getId());
        UserProductCatalogEntity catalog = userProductCatalogService.getOrCreateCatalog(userEntity);
        catalog.setStatus(CatalogStatus.SYNCING);
        catalog.setProgress(0);
        catalog.setLastProcessedId("");
        userProductCatalogService.save(catalog);

        try {
            String lastId = catalog.getLastProcessedId();
            int totalProcessed = 0;
            boolean firstBatch = true;
            int iteration = 1;

            do {
                log.debug("Итерация: [ {} ] загрузки товаров для пользователя: [ {} ]", iteration, userEntity.getId());
                // Загружаем следующую страницу
                ProductListResponse.ProductListResult result = ozonProductService.getProductListResult(lastId, 1000);

                if (result == null) {
                    log.debug("No result found for lastId: [ {} ]", lastId);
                    break;
                }

                List<ProductListResponse.ProductItem> page = result.getItems();

                if (page.isEmpty()) {
                    log.debug("No product items found for lastId: [ {} ]", lastId);
                    break;
                }

                // Маппим
                List<ProductEntity> productEntities = page.stream()
                        .map(item -> Mapper.mapProductEntity(item, catalog))
                        .toList();
                log.debug("Смаппили товаров: [ {} ]", productEntities.size());

                // Если это первый батч — очищаем старые товары
                if (firstBatch) {
                    productRepository.deleteAll(catalog.getProducts());
                    log.debug("Удалили все старые товары после первой итерации");
                    firstBatch = false;
                }

                productRepository.saveAll(productEntities);
                log.debug("Добавили товары после итерации: [ {} ]", iteration);

                Integer total = result.getTotal();
                log.debug("Total products: [ {} ]", total);

                totalProcessed += productEntities.size();

                // Обновляем прогресс
                catalog.setProgress(estimateProgress(totalProcessed, total));
                log.debug("Total processed products: [ {} ]", catalog.getProgress());

                catalog.setLastProcessedId(productEntities.getLast().getProductId().toString());
                userProductCatalogService.save(catalog);

                lastId = result.getLastId();
                iteration++;
            } while (lastId != null);

            catalog.setStatus(CatalogStatus.READY);
            catalog.setProgress(100);
            catalog.setLastSyncedAt(LocalDateTime.now());
            userProductCatalogService.save(catalog);

            log.debug("Синхронизация завершена для пользователя: [ {} ]", userEntity.getId());
        } catch (Exception e) {
            catalog.setStatus(CatalogStatus.FAILED);
            userProductCatalogService.save(catalog);
            log.error("Ошибка при синхронизации товаров: {}", e.getMessage(), e);
            throw e;
        }
    }

    private int estimateProgress(int processed, int total) {
        int percent = (int) ((processed * 100.0) / total);
        return Math.min(percent, 100);
    }
}
