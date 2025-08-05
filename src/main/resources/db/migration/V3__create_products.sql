CREATE TABLE products (
                          id BIGSERIAL PRIMARY KEY,
                          catalog_id BIGINT NOT NULL REFERENCES user_product_catalog(id) ON DELETE CASCADE,
                          product_id BIGINT NOT NULL,
                          offer_id VARCHAR(255),
                          name VARCHAR(255),
                          has_fbo_stocks BOOLEAN,
                          has_fbs_stocks BOOLEAN,
                          archived BOOLEAN,
                          is_discounted BOOLEAN,
                          last_synced_at TIMESTAMP,
                          CONSTRAINT uq_catalog_product UNIQUE (catalog_id, product_id)
);

-- Индекс для выборки всех товаров конкретного каталога
CREATE INDEX idx_products_catalog_id ON products(catalog_id);

-- Индекс для поиска по product_id (ускоряет обновления)
CREATE INDEX idx_products_product_id ON products(product_id);

-- Составной индекс для частых выборок и уникальности
CREATE INDEX idx_products_catalog_product_id ON products(catalog_id, product_id);
