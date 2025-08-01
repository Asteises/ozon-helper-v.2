package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.asteises.ozonhelper.model.ozon.ProductListRequest;
import ru.asteises.ozonhelper.model.ozon.ProductListResponse;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OzonProductService {

    private final WebClient ozonWebClient;

    private static final String PRODUCT_LIST_URL = "/v3/product/list";

    /**
     * Универсальный метод: если offerIds или productIds пустые — вернёт все товары
     */
    public List<ProductListResponse.ProductItem> getProducts(
            List<String> offerIds,
            List<String> productIds,
            String visibility
    ) {
        List<ProductListResponse.ProductItem> result = new ArrayList<>();
        String lastId = "";

        do {
            ProductListRequest request = ProductListRequest.builder()
                    .filter(ProductListRequest.Filter.builder()
                            .offer_id(offerIds)
                            .product_id(productIds)
                            .visibility(visibility)
                            .build())
                    .last_id(lastId)
                    .limit(1000) // максимум по API
                    .build();

            ProductListResponse response = ozonWebClient.post()
                    .uri(PRODUCT_LIST_URL)
//                    .header("Client-Id", clientId)
//                    .header("Api-Key", apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ProductListResponse.class)
                    .block();

            if (response == null || response.getItems() == null) {
                break;
            }

            result.addAll(response.getItems());
            lastId = response.getLast_id();

        } while (lastId != null && !lastId.isEmpty());

        return result;
    }
}
