package ru.asteises.ozonhelper.validator;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TelegramAuthValidator {

    public static boolean validate(String initData, String botToken) {
        log.debug("=== [TelegramAuthValidator] START VALIDATION ===");
        log.debug("Raw initData: {}", initData);
        log.debug("Bot token (masked): {}***", botToken.substring(0, 10));

        if (initData == null || initData.isBlank()) {
            log.warn("InitData is NULL or blank!");
            return false;
        }

        // Парсим параметры
        Map<String, String> dataMap = parseInitData(initData);
        log.debug("Parsed dataMap (without hash): {}", dataMap);

        String hash = dataMap.remove("hash");
        if (hash == null) {
            log.warn("Hash not found in initData!");
            return false;
        }
        log.debug("Received hash: {}", hash);

        // Формируем data_check_string
        String dataCheckString = dataMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");

        log.debug("Data check string (sorted): {}", dataCheckString);

        // SHA256 от bot token = secret key
        String secretKeyHex = hmacSha256(botToken.getBytes(StandardCharsets.UTF_8),
                "TelegramBotToken".getBytes(StandardCharsets.UTF_8));
        log.debug("Secret key (hex): {}", secretKeyHex);

        // HMAC-SHA256(data_check_string, secret_key)
        String computedHash = hmacSha256(
                dataCheckString.getBytes(StandardCharsets.UTF_8),
                hexStringToBytes(secretKeyHex)
        );
        log.debug("Computed hash: {}", computedHash);

        boolean valid = computedHash.equals(hash);
        log.debug("Validation result: {}", valid);

        log.debug("=== [TelegramAuthValidator] END VALIDATION ===");
        return valid;
    }

    private static Map<String, String> parseInitData(String initData) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = initData.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                map.put(kv[0], kv[1]);
            }
        }
        return map;
    }

    private static String hmacSha256(byte[] data, byte[] key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            byte[] result = mac.doFinal(data);
            return bytesToHex(result);
        } catch (Exception e) {
            log.error("Error during HMAC calculation", e);
            throw new RuntimeException(e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte[] hexStringToBytes(String hex) {
        int len = hex.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return bytes;
    }
}

