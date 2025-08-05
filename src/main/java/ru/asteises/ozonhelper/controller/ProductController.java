package ru.asteises.ozonhelper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.asteises.ozonhelper.model.CheckUserData;
import ru.asteises.ozonhelper.model.entities.UserEntity;
import ru.asteises.ozonhelper.model.entities.UserProductCatalogEntity;
import ru.asteises.ozonhelper.repository.UserRepository;
import ru.asteises.ozonhelper.service.ProductSyncService;
import ru.asteises.ozonhelper.service.UserProductCatalogService;
import ru.asteises.ozonhelper.validator.TelegramAuthValidator;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    @Value("${telegram.bot.token}")
    private String botToken;

    private final ProductSyncService productSyncService;
    private final UserProductCatalogService userProductCatalogService;
    private final UserRepository userRepository;

    @PostMapping("/sync/list")
    public ResponseEntity<String> startSync(@RequestBody CheckUserData checkUserData) {
        if (!TelegramAuthValidator.validateInitData(checkUserData.getTelegramInitData(), botToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }
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
