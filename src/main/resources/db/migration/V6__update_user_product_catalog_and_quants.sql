ALTER TABLE user_product_catalog
    ADD COLUMN uuid UUID;

ALTER TABLE product_quants
    ADD COLUMN uuid UUID;

ALTER TABLE products
    ADD COLUMN content_hash VARCHAR(64) NOT NULL DEFAULT 'UNKNOWN',
    ADD COLUMN quants_hash  VARCHAR(64),
    ADD COLUMN version      BIGINT;