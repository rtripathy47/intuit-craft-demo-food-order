-- Drop tables if they exist (for fresh creation)
DROP TABLE IF EXISTS order_item_fulfillment;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS `order`;
DROP TABLE IF EXISTS restaurant_items;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS items;

CREATE TABLE items (
    id bigserial NOT NULL,
    "name" varchar(255) NOT NULL,
    description varchar(255) NULL,
    CONSTRAINT items_pkey PRIMARY KEY (id),
    CONSTRAINT unique_item_name UNIQUE (name)
);

CREATE TABLE restaurants (
    id bigserial NOT NULL,
    "name" varchar(255) NOT NULL,
    rating float8 NULL,
    current_load int4 NOT NULL DEFAULT 0,
    max_capacity int4 NOT NULL,
    "version" int4 NOT NULL,
    CONSTRAINT restaurants_pkey PRIMARY KEY (id),
    CONSTRAINT restaurants_rating_check CHECK (rating >= 0 AND rating <= 5),
    CONSTRAINT unique_restaurant_name UNIQUE (name)
);

CREATE TABLE restaurant_items (
    id bigserial NOT NULL,
    restaurant_id int8 NOT NULL,
    item_id int8 NOT NULL,
    price float8 NOT NULL,
    "version" int8 NULL,
    strategy varchar(255) NOT NULL DEFAULT 'lower_cost',  -- Added strategy column
    CONSTRAINT restaurant_items_pkey PRIMARY KEY (id),
    CONSTRAINT unique_restaurant_item UNIQUE (restaurant_id, item_id)
);

-- Foreign keys for restaurant_items
ALTER TABLE restaurant_items ADD CONSTRAINT restaurant_items_item_id_fkey FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE;
ALTER TABLE restaurant_items ADD CONSTRAINT restaurant_items_restaurant_id_fkey FOREIGN KEY (restaurant_id) REFERENCES restaurants(id) ON DELETE CASCADE;

CREATE TABLE orders (
    id bigserial NOT NULL,
    customer_id int4 NOT NULL,
    status varchar(255) NOT NULL,
    order_time timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT orders_pkey PRIMARY KEY (id)
);

CREATE TABLE order_items (
    id bigserial NOT NULL,
    order_id int8 NOT NULL,
    item_id int8 NOT NULL,
    quantity int4 NOT NULL,
    CONSTRAINT order_items_pkey PRIMARY KEY (id)
);

-- Foreign keys for order_items
ALTER TABLE order_items ADD CONSTRAINT order_items_item_id_fkey FOREIGN KEY (item_id) REFERENCES items(id);
ALTER TABLE order_items ADD CONSTRAINT order_items_order_id_fkey FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE;

-- Create the 'order_item_fulfillment' table
CREATE TABLE order_item_fulfillments (
    id bigserial NOT NULL,
    order_item_id int8 NOT NULL,
    restaurant_id int8 NOT NULL,
    price_per_unit float8 NULL,
    quantity_fulfilled int4 NULL,
    CONSTRAINT order_item_fulfillments_pkey PRIMARY KEY (id)
);

-- Foreign keys for order_item_fulfillments
ALTER TABLE order_item_fulfillments ADD CONSTRAINT order_item_fulfillments_order_item_id_fkey FOREIGN KEY (order_item_id) REFERENCES order_items(id) ON DELETE CASCADE;
ALTER TABLE order_item_fulfillments ADD CONSTRAINT order_item_fulfillments_restaurant_id_fkey FOREIGN KEY (restaurant_id) REFERENCES restaurants(id);

-- Create necessary indexes
CREATE INDEX idx_items_name ON items USING btree (name);
CREATE INDEX idx_restaurant_name ON restaurants USING btree (name);
CREATE INDEX idx_restaurant_items_item_id ON restaurant_items USING btree (item_id);
CREATE INDEX idx_restaurant_items_restaurant_id ON restaurant_items USING btree (restaurant_id);
CREATE INDEX idx_orders_customer_id ON orders USING btree (customer_id);
CREATE INDEX idx_orders_order_time ON orders USING btree (order_time);
CREATE INDEX idx_order_items_item_id ON order_items USING btree (item_id);
CREATE INDEX idx_order_items_order_id ON order_items USING btree (order_id);
CREATE INDEX idx_order_item_fulfillments_order_item_id ON order_item_fulfillments USING btree (order_item_id);
CREATE INDEX idx_order_item_fulfillments_restaurant_id ON order_item_fulfillments USING btree (restaurant_id);
