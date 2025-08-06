CREATE TABLE product_quants (
                                id BIGSERIAL PRIMARY KEY,
                                product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
                                quant_code VARCHAR(100) NOT NULL,
                                quant_size INT
);

-- Индекс для выборки квантов по продукту
CREATE INDEX idx_quants_product_id ON product_quants(product_id);

-- Индекс для уникальных квантов внутри продукта (если нужно запретить дубли quant_code)
CREATE UNIQUE INDEX uq_quants_product_code ON product_quants(product_id, quant_code);