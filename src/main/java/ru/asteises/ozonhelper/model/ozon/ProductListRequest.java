package ru.asteises.ozonhelper.model.ozon;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductListRequest {
    private Filter filter;
    private String last_id;
    private int limit;

    @Data
    @Builder
    public static class Filter {
        private List<String> offer_id;
        private List<String> product_id;
        private String visibility; // "ALL", "VISIBLE", "INVISIBLE"
    }
}
