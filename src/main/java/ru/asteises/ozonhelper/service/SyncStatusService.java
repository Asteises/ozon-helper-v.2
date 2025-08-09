package ru.asteises.ozonhelper.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
    private final ObjectMapper objectMapper = new ObjectMapper();


    public void registerEmitter(String taskId, SseEmitter emitter) {
        emitterMap.put(taskId, emitter);
        emitter.onCompletion(() -> emitterMap.remove(taskId));
        emitter.onTimeout(() -> emitterMap.remove(taskId));
        try {
            emitter.send(SseEmitter.event().name("connected").data("ok"));
        } catch (Exception ex) {
            log.error("Error while sending emitter", ex);
        }
        log.info("Emitter registered: [ {} ]", emitter);
    }

    public void sendUpdate(String taskId, SyncStatusInfo info) {
        log.info("Sending update to task: [ {} ]", taskId);
        SseEmitter emitter = emitterMap.get(taskId);
        log.debug("Found emitter: [ {} ]", emitter);
        if (emitter != null) {
            try {
                log.info("Sending update to task: [ {} ]", taskId);
                String json = objectMapper.writeValueAsString(info);
                log.debug("Sending update json: [ {} ]", json);

                emitter.send(SseEmitter.event()
                        .name("progress")
                        .data(json, MediaType.APPLICATION_JSON));

                log.info("After sending update to task: [ {} ]", taskId);

                if (info.getCatalogStatus() == CatalogStatus.READY || info.getCatalogStatus() == CatalogStatus.FAILED) {
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
