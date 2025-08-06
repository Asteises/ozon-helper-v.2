package ru.asteises.ozonhelper.validator;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class TelegramAuthValidator {

    public static Boolean validateInitData(String initData, String botToken) {
        return TelegramAuthValidator.validate(initData, botToken);
    }

    /**
     * Валидация initData из Telegram MiniApp
     *
     * @param initData строка из window.Telegram.WebApp.initData
     * @param botToken полный токен бота от BotFather (ID:секрет)
     * @return true, если данные подлинные
     */
    private static boolean validate(String initData, String botToken) {
        log.debug("=== [TelegramAuthValidator] START VALIDATION ===");

        if (initData == null || initData.isBlank()) {
            log.warn("InitData is NULL or blank!");
            return false;
        }

//        log.debug("Raw initData: {}", initData);

        Map<String, String> params = Arrays.stream(initData.split("&"))
                .map(s -> s.split("=", 2))
                .collect(Collectors.toMap(
                        arr -> arr[0],
                        arr -> URLDecoder.decode(arr[1], StandardCharsets.UTF_8)
                ));

//        log.debug("Parsed params: {}", params);

        String dataCheckString = params.entrySet().stream()
                .filter(e -> !"hash".equals(e.getKey()))
                .sorted(Map.Entry.comparingByKey())
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));

//        log.debug("DataCheckString: {}", dataCheckString);

        try {
            byte[] secretKey = hmacSha256("WebAppData".getBytes(StandardCharsets.UTF_8), botToken);
            byte[] hashBytes = hmacSha256(secretKey, dataCheckString);
            String calculatedHash = bytesToHex(hashBytes);
            if (!calculatedHash.equalsIgnoreCase(params.get("hash"))) {
                throw new SecurityException("Invalid MiniApp signature");
            }
            long authDate = Long.parseLong(params.get("auth_date"));
            if (Instant.now().getEpochSecond() - authDate > 300) {
                throw new SecurityException("auth_date expired");
            }
        } catch (Exception e) {
            log.error("Failed to hmac sha256", e);
            throw new RuntimeException(e);
        }
        return true;
    }

    private static byte[] hmacSha256(byte[] key, String data) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec keySpec = new SecretKeySpec(key, "HmacSHA256");
        mac.init(keySpec);
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

}
