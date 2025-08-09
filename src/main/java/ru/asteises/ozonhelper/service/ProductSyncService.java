package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.asteises.ozonhelper.enums.CatalogStatus;
import ru.asteises.ozonhelper.enums.SyncStatus;
import ru.asteises.ozonhelper.mapper.Mapper;
import ru.asteises.ozonhelper.model.SyncStatusInfo;
import ru.asteises.ozonhelper.model.entities.ProductEntity;
import ru.asteises.ozonhelper.model.entities.UserEntity;
import ru.asteises.ozonhelper.model.entities.UserProductCatalogEntity;
import ru.asteises.ozonhelper.model.ozon.ProductListResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSyncService {

    private final ProductService productService;
    private final SyncStatusService syncStatusService;
    private final OzonProductService ozonProductService;
    private final ProductUpsertService productUpsertService;
    private final UserProductCatalogService userProductCatalogService;


    public void syncUserProductsAsync(UserEntity userEntity, String taskId) {
        syncUserProducts(userEntity, taskId);
    }

    public void syncUserProducts(UserEntity userEntity, String taskId) {
        Long userTgId = userEntity.getTelegramUserId();
        log.debug("Sync products for user: [ {} ]", userTgId);

        // 1. Проверяем наличие каталога пользователя или создаем новый
        syncStatusService.sendUpdate(taskId, getProcessInfo(taskId, 0));

        UserProductCatalogEntity catalog = userProductCatalogService.getOrCreateCatalog(userEntity);
        log.debug("Catalog products: [ {} ]", catalog.getProducts() != null ? catalog.getProducts().size() : 0);
        catalog.setStatus(CatalogStatus.SYNCING);
        catalog.setProgress(0);
        catalog.setLastProcessedId("");
        userProductCatalogService.save(catalog);

        try {
            // 2. Загружаем все товары из Озон
            CompletableFuture<List<ProductEntity>> futureProducts = getProductsFromOzonAsync(taskId, userTgId, catalog);

            // 3. Дожидаемся результата
            List<ProductEntity> products = futureProducts.join();

            if (products.isEmpty()) {
                log.debug("Product list from Ozon is empty --> sync finished try it later");
                catalog.setStatus(CatalogStatus.READY);
                catalog.setProgress(100);
                catalog.setLastSyncedAt(LocalDateTime.now());
                userProductCatalogService.save(catalog);
                return;
            }

            // 4. Проверяем новый список - сохраняем новые и обновленные товары
            productUpsertService.upsertProducts(catalog, products);
            catalog.setProgress(75);
            syncStatusService.sendUpdate(taskId, getProcessInfo(taskId, 89));

            // 5. Обновляем каталог пользователя
            catalog.setStatus(CatalogStatus.READY);
            catalog.setProgress(100);
            catalog.setLastSyncedAt(LocalDateTime.now());
            userProductCatalogService.save(catalog);

            catalog.setProgress(100);
            syncStatusService.sendUpdate(taskId, getProcessInfo(taskId, 100));

            log.debug("Sync products end for user: [ {} ]", userEntity.getId());
        } catch (Exception e) {
            catalog.setStatus(CatalogStatus.FAILED);
            userProductCatalogService.save(catalog);
            log.error("Error while sync products: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Async("taskExecutor")
    public CompletableFuture<List<ProductEntity>> getProductsFromOzonAsync(String taskId, Long userId, UserProductCatalogEntity catalog) {
        List<ProductEntity> allProducts = new ArrayList<>();
        String lastId = catalog.getLastProcessedId();
        int total = 0;
        int totalProcessed = 0;
        int iteration = 1;

        do {
            log.debug("Iteration: [ {} ] get products from Ozon for user: [ {} ]", iteration, userId);

            // Загружаем страницу товаров из Ozon
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
            log.debug("Mapping products: [ {} ]", productEntities.size());

            // Складываем в общий список
            allProducts.addAll(productEntities);
            log.debug("Total products for save: [ {} ]", allProducts.size());

            total = result.getTotal();
            log.debug("Total products: [ {} ]", total);

            totalProcessed += productEntities.size();

            // Обновляем прогресс
            int progress = estimateProgress(totalProcessed, total);
            catalog.setProgress(progress);
            log.debug("Total processed products progress: [ {} ]", catalog.getProgress());

            lastId = result.getLastId();
            iteration++;

            syncStatusService.sendUpdate(taskId, getProcessInfo(taskId, progress));

        } while (allProducts.size() != total);
        log.debug("Total products get from Ozon: [ {} ]", allProducts.size());
        return CompletableFuture.completedFuture(allProducts);
    }

    private int estimateProgress(int processed, int total) {
        if (total <= 0) {
            return 0;
        }
        // Доля загрузки — максимум 80%
        double loadPart = (processed * 50.0) / total;
        return Math.min((int) loadPart, 50);
    }


    private SyncStatusInfo getProcessInfo(String taskId, int processed) {
        return SyncStatusInfo.builder()
                .taskId(taskId)
                .catalogStatus(CatalogStatus.SYNCING)
                .syncStatus(SyncStatus.RUNNING)
                .progress(processed)
                .startedAt(LocalDateTime.now())
                .build();
    }
}
