package ru.asteises.ozonhelper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.asteises.ozonhelper.model.ozon.ProductListResponse;
import ru.asteises.ozonhelper.service.OzonProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final OzonProductService ozonProductService;

    @GetMapping
    public List<ProductListResponse.ProductItem> getAllProducts(
            @RequestParam(required = false) List<String> offerIds,
            @RequestParam(required = false) List<String> productIds,
            @RequestParam(defaultValue = "ALL") String visibility
    ) {
        return ozonProductService.getProducts(
                offerIds == null ? List.of() : offerIds,
                productIds == null ? List.of() : productIds,
                visibility
        );
    }
}
