CREATE TABLE user_product_catalog (
                                      id BIGSERIAL PRIMARY KEY,
                                      user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                      total_products INT NOT NULL DEFAULT 0,
                                      last_synced_at TIMESTAMP,
                                      status VARCHAR(50) DEFAULT 'READY',
                                      CONSTRAINT uq_user_catalog UNIQUE (user_id)
);

-- Индекс для быстрого поиска каталога по пользователю
CREATE INDEX idx_catalog_user_id ON user_product_catalog(user_id);
