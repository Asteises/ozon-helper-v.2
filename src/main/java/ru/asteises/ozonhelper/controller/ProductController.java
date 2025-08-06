package ru.asteises.ozonhelper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.asteises.ozonhelper.model.CheckUserData;
import ru.asteises.ozonhelper.model.entities.UserEntity;
import ru.asteises.ozonhelper.model.entities.UserProductCatalogEntity;
import ru.asteises.ozonhelper.repository.UserRepository;
import ru.asteises.ozonhelper.service.ProductSyncService;
import ru.asteises.ozonhelper.service.UserProductCatalogService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductSyncService productSyncService;
    private final UserProductCatalogService userProductCatalogService;
    private final UserRepository userRepository;

    @PostMapping("/sync/list")
    public ResponseEntity<String> startSync(@RequestBody CheckUserData checkUserData) {
        UserEntity user = userRepository.findByTelegramUserId(checkUserData.getTelegramUserId())
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден"));
        productSyncService.syncUserProductsAsync(user);

        return ResponseEntity.accepted().body("Синхронизация запущена");
    }

    @GetMapping("/status/{telegramUserId}")
    public ResponseEntity<Map<String, Object>> getSyncStatus(@PathVariable Long telegramUserId) {
        UserProductCatalogEntity catalog = userProductCatalogService
                .getByTelegramUserId(telegramUserId)
                .orElseThrow(() -> new IllegalArgumentException("Каталог не найден"));
        return ResponseEntity.ok(Map.of(
                "status", catalog.getStatus(),
                "progress", catalog.getProgress(),
                "lastSyncedAt", catalog.getLastSyncedAt()
        ));
    }
}
