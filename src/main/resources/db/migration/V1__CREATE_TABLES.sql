CREATE TABLE smartlistuser (
                               id BIGSERIAL PRIMARY KEY,
                               name VARCHAR(255) NOT NULL,
                               email VARCHAR(255) UNIQUE NOT NULL,
                               avatar_url VARCHAR(500)
);

CREATE TABLE shopping_list (
                               id BIGSERIAL PRIMARY KEY,
                               title VARCHAR(255) NOT NULL,
                               status VARCHAR(50),
                               total NUMERIC(10,2) DEFAULT 0,
                               user_id BIGINT NOT NULL,
                               CONSTRAINT fk_shoppinglist_user FOREIGN KEY (user_id) REFERENCES smartlistuser (id) ON DELETE CASCADE
);

CREATE TABLE product_item (
                              id BIGSERIAL PRIMARY KEY,
                              name VARCHAR(255) NOT NULL,
                              quantity BIGINT,
                              price NUMERIC(10,2),
                              checked BOOLEAN DEFAULT FALSE,
                              shopping_list_id BIGINT NOT NULL,
                              CONSTRAINT fk_item_list FOREIGN KEY (shopping_list_id) REFERENCES shopping_list (id) ON DELETE CASCADE
);
