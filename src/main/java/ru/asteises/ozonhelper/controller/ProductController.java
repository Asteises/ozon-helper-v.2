package ru.asteises.ozonhelper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.asteises.ozonhelper.model.CheckUserData;
import ru.asteises.ozonhelper.model.entities.UserEntity;
import ru.asteises.ozonhelper.model.entities.UserProductCatalogEntity;
import ru.asteises.ozonhelper.service.ProductSyncService;
import ru.asteises.ozonhelper.service.UserProductCatalogService;
import ru.asteises.ozonhelper.service.UserService;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final ProductSyncService productSyncService;
    private final UserProductCatalogService userProductCatalogService;
    private final UserService userService;

    @PostMapping("/sync/list")
    public ResponseEntity<String> startSync(@RequestBody CheckUserData checkUserData) {
        log.info("Start sync product with init data: {}", checkUserData);
        UserEntity user = userService.getUserOrNull(checkUserData.getTelegramUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }
        productSyncService.syncUserProductsAsync(user);

        return ResponseEntity.accepted().body("Синхронизация запущена");
    }

    @GetMapping("/sync/status")
    public ResponseEntity<Map<String, Object>> getSyncStatus(@RequestBody CheckUserData checkUserData) {
        UserProductCatalogEntity catalog = userProductCatalogService
                .getByTelegramUserId(checkUserData.getTelegramUserId())
                .orElseThrow(() -> new IllegalArgumentException("Каталог не найден"));
        return ResponseEntity.ok(Map.of(
                "status", catalog.getStatus(),
                "progress", catalog.getProgress(),
                "lastSyncedAt", catalog.getLastSyncedAt()
        ));
    }
}
