package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.asteises.ozonhelper.model.ozon.ProductListRequest;
import ru.asteises.ozonhelper.model.ozon.ProductListResponse;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class OzonProductService {

    private final WebClient ozonWebClient;

    private static final String PRODUCT_LIST_URL = "/v3/product/list";


    public ProductListResponse.ProductListResult getProductListResult(String lastId, int limit) {
        ProductListRequest request = ProductListRequest.builder()
                .filter(ProductListRequest.Filter.builder()
                        .offerId(List.of())
                        .productId(List.of())
                        .visibility("ALL")
                        .build())
                .lastId(lastId)
                .limit(limit)
                .build();

        ProductListResponse response = ozonWebClient.post()
                .uri(PRODUCT_LIST_URL)
                .header("Client-Id", "125357")
                .header("Api-Key", "700ee6cf-efd3-400f-974d-449cb36ac2a3")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ProductListResponse.class)
                .block();

        return response == null || response.getResult() == null
                ? null
                : response.getResult();
    }
}
