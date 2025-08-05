package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.asteises.ozonhelper.model.ozon.ProductListRequest;
import ru.asteises.ozonhelper.model.ozon.ProductListResponse;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class OzonProductService {

    private final WebClient ozonWebClient;

    private static final String PRODUCT_LIST_URL = "/v3/product/list";
    private static final Integer LIMIT = 1000;

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
                    .limit(LIMIT) // максимум по API
                    .build();

            ProductListResponse response = ozonWebClient.post()
                    .uri(PRODUCT_LIST_URL)
                    .header("Client-Id", "125357")
                    .header("Api-Key", "700ee6cf-efd3-400f-974d-449cb36ac2a3")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(ProductListResponse.class)
                    .block();

            log.info("Response from Ozon: {}", response);

            if (response == null || response.getResult() == null) {
                log.debug("No products found");
                break;
            }

            ProductListResponse.ProductListResult respResult = response.getResult();
            log.debug("Products on page found: [ {} ]", respResult.getItems().size());

            result.addAll(respResult.getItems());
            log.debug("Total products found: [ {} ]", result.size());

            lastId = respResult.getLastId();

        } while (lastId != null && !lastId.isEmpty());

        return result;
    }
}
