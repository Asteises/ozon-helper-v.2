package ru.asteises.ozonhelper.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import ru.asteises.ozonhelper.enums.CatalogStatus;
import ru.asteises.ozonhelper.model.SyncStatusInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class SyncStatusService {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    public void registerEmitter(String taskId, SseEmitter emitter) {
        emitterMap.put(taskId, emitter);
        emitter.onCompletion(() -> emitterMap.remove(taskId));
        emitter.onTimeout(() -> emitterMap.remove(taskId));
    }

    public void sendUpdate(String taskId, SyncStatusInfo info) {
        SseEmitter emitter = emitterMap.get(taskId);
        if (emitter != null) {
            try {
                log.info("Sending update to task: [ {} ]", taskId);
                emitter.send(info);
                if (info.getStatus() == CatalogStatus.READY || info.getStatus() == CatalogStatus.FAILED) {
                    emitter.complete();
                    emitterMap.remove(taskId);
                    log.info("Task: [ {} ] completed", taskId);
                }
            } catch (Exception e) {
                log.error("Error while sending update to task: [ {} ]", taskId, e);
                emitter.completeWithError(e);
                emitterMap.remove(taskId);
            }
        }
    }
}
