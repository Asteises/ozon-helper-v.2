package ru.asteises.ozonhelper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.asteises.ozonhelper.model.RequestData;
import ru.asteises.ozonhelper.model.entities.UserEntity;
import ru.asteises.ozonhelper.service.ProductSyncService;
import ru.asteises.ozonhelper.service.SyncStatusService;
import ru.asteises.ozonhelper.service.UserService;

import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    private final UserService userService;
    private final SyncStatusService syncStatusService;
    private final ProductSyncService productSyncService;


    @PostMapping("/sync/list")
    public ResponseEntity<String> startSync(@RequestBody RequestData requestData) {
        log.info("Start sync product with init data: {}", requestData);
        UserEntity user = userService.getUserOrNull(requestData.getTelegramUserId());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(String.format("User with tg id: [ %s ] not found", requestData.getTelegramUserId()));
        }

        productSyncService.syncUserProductsAsync(user, requestData.getTaskId());

        return ResponseEntity.accepted().body("Sync completed");
    }

    @GetMapping("/sync/stream")
    public SseEmitter streamSyncStatus(@RequestParam String taskId) {
        log.info("Start sync product with task id: [ {} ]", taskId);
        SseEmitter emitter = new SseEmitter(0L);
        syncStatusService.registerEmitter(taskId, emitter);
        return emitter;
    }

}
