package ru.asteises.ozonhelper.utils;

import ru.asteises.ozonhelper.model.entities.ProductEntity;
import ru.asteises.ozonhelper.model.entities.ProductQuantEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class HashesUtils {

    public static String sha256(String s) {
        try {
            var md = java.security.MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(s.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private static String nz(Object v) { return v == null ? "" : v.toString().trim(); }
    private static String nzBool(Boolean b) { return b != null && b ? "1" : "0"; }

    /** Хеш «контента» товара без quants. */
    public static String productContentHash(ProductEntity p) {
        // Важно: порядок полей фиксированный
        String raw = String.join("|",
                // бизнес-ключ (не обязателен в хеше, но помогает стабилизировать строку)
                "pid=" + nz(p.getProductId()),
                "offer=" + nz(p.getOfferId()),
                "fbo=" + nzBool(p.getHasFboStocks()),
                "fbs=" + nzBool(p.getHasFbsStocks()),
                "arch=" + nzBool(p.getArchived()),
                "disc=" + nzBool(p.getIsDiscounted())
                // lastSyncedAt в хеш НЕ включаем, это тех.поле
        );
        return sha256(raw);
    }

    /** Хеш состава quants (код/размер). Меняй под свою модель. */
    public static String quantsHash(List<ProductQuantEntity> quants) {
        if (quants == null || quants.isEmpty()) return "";
        // Стабилизируем порядок — сперва сортируем по ключу
        var list = new ArrayList<>(quants);
        list.sort(Comparator
                .comparing(ProductQuantEntity::getQuantCode, Comparator.nullsFirst(String::compareTo))
                .thenComparing(ProductQuantEntity::getQuantSize));

        String raw = list.stream()
                .map(q -> nz(q.getQuantCode()) + ":" + nz(q.getQuantSize()))
                .collect(java.util.stream.Collectors.joining("|"));
        return sha256(raw);
    }

}
