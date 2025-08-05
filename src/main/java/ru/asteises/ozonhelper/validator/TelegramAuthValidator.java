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

    public static Boolean validate(String initData, String botToken) {
        return validateInitData(initData, botToken);
    }

    /**
     * Валидация initData из Telegram MiniApp
     *
     * @param initData строка из window.Telegram.WebApp.initData
     * @param botToken полный токен бота от BotFather (ID:секрет)
     * @return true, если данные подлинные
     */
    private static boolean validateInitData(String initData, String botToken) {
        log.debug("=== [TelegramAuthValidator] START VALIDATION ===");

        if (initData == null || initData.isBlank()) {
            log.warn("InitData is NULL or blank!");
            return false;
        }

        /*
        EXAMPLE:

        telegramInitData=
        query_id=AAG8OosQAAAAALw6ixDVqQDA&
        user=%7B%22id%22%3A277559996%2C%22
        first_name%22%3A%22Filipp%22%2C%22
        last_name%22%3A%22%22%2C%22
        username%22%3A%22Asteises%22%2C%22
        language_code%22%3A%22en%22%2C%22
        is_premium%22%3Atrue%2C%22
        allows_write_to_pm%22%3Atrue%2C%22
        photo_url%22%3A%22https%3A%5C%2F%5C%2Ft.me%5C%2Fi%5C%2Fuserpic%5C%2F320%5C%2FstbNrthH9JEfLRGgwMNIq14tCNLWOpy-zlbae0iS7JE.svg%22%7D&
        auth_date=1754308649&signature=OCpDNB9lozG1Gkem1idDhPTvHFffOz8VJR4IAyHzJ7hXne3V3uwu9FhOfkcSQ35iyljTTcaSSsf1f_C1NkofDA&
        hash=7951d1acfc4ed0309f988a9e20ce0bd7de6f03fb7019f0a93aefe19e12fc3a67
         */

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
