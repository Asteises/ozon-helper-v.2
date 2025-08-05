package ru.asteises.ozonhelper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.asteises.ozonhelper.model.ozon.ProductListResponse;
import ru.asteises.ozonhelper.service.OzonProductService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
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

    @ResponseBody
    @GetMapping("/list/test")
    public ResponseEntity<List<ProductListResponse.ProductItem>> getProductListTest() {
        log.info("Try to get product list for user tg id");
        return ResponseEntity.ok(ozonProductService.getProducts(
                List.of(),
                List.of(),
                "ALL"
        ));
    }
}
