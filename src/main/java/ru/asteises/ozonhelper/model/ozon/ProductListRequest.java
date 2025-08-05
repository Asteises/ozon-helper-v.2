package ru.asteises.ozonhelper.model.ozon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductListRequest {
    private Filter filter;
    @JsonProperty("last_id")
    private String lastId;
    private Integer limit;

    @Data
    @Builder
    public static class Filter {
        @JsonProperty("offer_id")
        private List<String> offerId;
        @JsonProperty("product_id")
        private List<Long> productId;
        private String visibility; // "ALL", "VISIBLE", "INVISIBLE"
    }
}
