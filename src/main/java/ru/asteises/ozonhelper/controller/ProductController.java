package ru.asteises.ozonhelper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.asteises.ozonhelper.model.CheckUserData;
import ru.asteises.ozonhelper.model.ozon.ProductListResponse;
import ru.asteises.ozonhelper.service.OzonProductService;
import ru.asteises.ozonhelper.validator.TelegramAuthValidator;

import java.util.Collections;
import java.util.List;

//https://asteises.ru/dev/bot/ozon/helper
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {

    @Value("${telegram.bot.token}")
    private String botToken;

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
    @PostMapping("/list")
    public ResponseEntity<List<ProductListResponse.ProductItem>> getProductList(@RequestBody CheckUserData checkUserData) {
        log.info("Try to get product list for user tg id: [ {} ]", checkUserData.getTelegramUserId());
        if (!TelegramAuthValidator.validate(checkUserData.getTelegramInitData(), botToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Collections.emptyList());
        }
        return ResponseEntity.ok(ozonProductService.getProducts(
                List.of(),
                List.of(),
                "ALL"
        ));
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
