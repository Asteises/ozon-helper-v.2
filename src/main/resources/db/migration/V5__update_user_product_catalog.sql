ALTER TABLE user_product_catalog
    ADD COLUMN progress INT DEFAULT 0,
    ADD COLUMN last_processed_id VARCHAR(255);