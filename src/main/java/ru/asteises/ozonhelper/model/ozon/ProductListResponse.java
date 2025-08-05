package ru.asteises.ozonhelper.model.ozon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProductListResponse {
    @JsonProperty("result")
    private ProductListResult result;

    @Data
    public static class ProductListResult {
        @JsonProperty("items")
        private List<ProductItem> items;
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("last_id")
        private String lastId;
    }

    @Data
    public static class ProductItem {
        @JsonProperty("product_id")
        private Integer productId;
        @JsonProperty("offer_id")
        private String offerId;
        @JsonProperty("has_fbo_stocks")
        private Boolean hasFboStocks;
        @JsonProperty("has_fbs_stocks")
        private Boolean hasFbsStocks;
        @JsonProperty("archived")
        private Boolean archived;
        @JsonProperty("is_discounted")
        private Boolean isDiscounted;
        @JsonProperty("quants")
        private List<ProductQuant> quants;
    }

    @Data
    public static class ProductQuant {
        @JsonProperty("quant_code")
        private String quantCode;
        @JsonProperty("quant_size")
        private Integer quantSize;
    }
}
