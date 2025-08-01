package ru.asteises.ozonhelper.model.ozon;

import lombok.Data;

import java.util.List;

@Data
public class ProductListResponse {
    private List<ProductItem> items;
    private String last_id;

    @Data
    public static class ProductItem {
        private String product_id;
        private String offer_id;
        private String name;
        private String status;
    }
}
