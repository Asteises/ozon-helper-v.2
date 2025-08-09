package ru.asteises.ozonhelper.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.asteises.ozonhelper.model.entities.ProductEntity;
import ru.asteises.ozonhelper.repository.ProductRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void saveAll(List<ProductEntity> products) {
        log.debug("Try saving products: [ {} ]", products.size());
        productRepository.saveAll(products);
    }

    @Transactional
    public int deleteAllByCatalogId(Long catalogId) {
        return productRepository.deleteByCatalogId(catalogId);
    }

    @Transactional
    public void deleteAll(List<ProductEntity> products) {
        log.debug("Try delete products: [ {} ]", products.size());
        productRepository.deleteAll(products);
        log.debug("After delete products: [ {} ]", productRepository.count());
    }

    @Transactional
    public void flush() {
        log.debug("Try flush products: [ {} ]", productRepository.count());
        productRepository.flush();
        log.debug("Flush products: [ {} ]", productRepository.count());
    }
}
