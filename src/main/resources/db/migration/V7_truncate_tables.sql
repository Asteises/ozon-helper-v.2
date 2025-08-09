SET session_replication_role = replica;
TRUNCATE TABLE
    products,
    users,
    user_product_catalog,
    product_quants
    RESTART IDENTITY CASCADE;
SET session_replication_role = DEFAULT;