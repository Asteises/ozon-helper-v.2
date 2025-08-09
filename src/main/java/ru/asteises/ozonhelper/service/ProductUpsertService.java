package ru.asteises.ozonhelper.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asteises.ozonhelper.model.entities.ProductEntity;
import ru.asteises.ozonhelper.model.entities.ProductQuantEntity;
import ru.asteises.ozonhelper.model.entities.UserProductCatalogEntity;
import ru.asteises.ozonhelper.repository.ProductRepository;
import ru.asteises.ozonhelper.utils.HashesUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ProductUpsertService {

    private final ProductRepository productRepository;
    private final EntityManager em;

    @Transactional
    public void upsertProducts(UserProductCatalogEntity catalog,
                               List<ProductEntity> incoming) {
        long catalogId = catalog.getId();

        // 1) Соберём список productId из входящих
        List<Long> ids = incoming.stream()
                .map(ProductEntity::getProductId)
                .distinct()
                .toList();

        // 2) Прочитаем существующие за один запрос
        List<ProductEntity> existing = productRepository
                .findByCatalogIdWithQuants(catalogId);

        // 3) Индекс по productId
        Map<Long, ProductEntity> byPid = existing.stream()
                .collect(java.util.stream.Collectors.toMap(ProductEntity::getProductId, e -> e));

        // 4) Разделим на новые и потенциально изменившиеся
        List<ProductEntity> toInsert = new ArrayList<>();
        List<ProductEntity> toUpdate = new ArrayList<>();

        for (ProductEntity inc : incoming) {
            inc.setCatalog(catalog); // обязательно
            // гарантируем актуальные хеши (если маппер уже сделал — не страшно)
            inc.setContentHash(HashesUtils.productContentHash(inc));
            inc.setQuantsHash(HashesUtils.quantsHash(inc.getQuants()));

            ProductEntity cur = byPid.get(inc.getProductId());
            if (cur == null) {
                // новый — ensure id null
                inc.setId(null);
                toInsert.add(inc);
            } else {
                // сравнить хеши
                boolean contentChanged = !Objects.equals(cur.getContentHash(), inc.getContentHash());
                boolean quantsChanged  = !Objects.equals(cur.getQuantsHash(),  inc.getQuantsHash());

                if (contentChanged || quantsChanged) {
                    // обновляем только изменившиеся поля
                    applyDiff(cur, inc, contentChanged, quantsChanged);
                    // пересчёт (на случай applyDiff что-то изменил)
                    cur.setContentHash(HashesUtils.productContentHash(cur));
                    cur.setQuantsHash(HashesUtils.quantsHash(cur.getQuants()));
                    cur.setLastSyncedAt(LocalDateTime.now());
                    toUpdate.add(cur);
                }
            }
        }

        // 5) Сохраняем пачками
        if (!toInsert.isEmpty()) productRepository.saveAll(toInsert);
        if (!toUpdate.isEmpty()) productRepository.saveAll(toUpdate);
        // при больших объёмах можно включить батчи в hibernate.properties
    }

    /** Обновляем только изменившиеся поля; quants — через orphanRemoval. */
    private void applyDiff(ProductEntity target, ProductEntity incoming,
                           boolean contentChanged, boolean quantsChanged) {
        if (contentChanged) {
            target.setOfferId(incoming.getOfferId());
            target.setName(incoming.getName());
            target.setHasFboStocks(incoming.getHasFboStocks());
            target.setHasFbsStocks(incoming.getHasFbsStocks());
            target.setArchived(incoming.getArchived());
            target.setIsDiscounted(incoming.getIsDiscounted());
        }
        if (quantsChanged) {
            // Простая стратегия — заменить (orphanRemoval=true сам удалит старые)
            target.getQuants().clear();
            if (incoming.getQuants() != null) {
                for (ProductQuantEntity q : incoming.getQuants()) {
                    q.setId(null);          // если у кванта есть id
                    q.setProduct(target);
                    target.getQuants().add(q);
                }
            }
        }
    }
}
