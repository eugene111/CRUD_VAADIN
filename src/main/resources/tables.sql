CREATE TABLE customers(id bigint IDENTITY NOT NULL, name VARCHAR(20) NOT NULL, second_name VARCHAR(20), surname VARCHAR(20) NOT NULL, telephone VARCHAR(20) NOT NULL, PRIMARY KEY(id));
CREATE TABLE orders(id bigint IDENTITY NOT NULL, description VARCHAR(100) NOT NULL, customer_id bigint NOT NULL, customer_surname VARCHAR(20) NOT NULL, order_date DATE, completion_date DATE, price FLOAT  NOT NULL, status VARCHAR(25)  NOT NULL, PRIMARY KEY (id));