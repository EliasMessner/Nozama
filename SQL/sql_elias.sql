CREATE TABLE store(
	s_name VARCHAR,
	street VARCHAR,
	zip INT,
	PRIMARY KEY(s_name, street, zip)
);

CREATE TABLE store_inventory(
	artID INT PRIMARY KEY,
    product INT REFERENCES product(prod_number),
	store_name VARCHAR,
	store_street VARCHAR,
	store_zip VARCHAR,
	article_condition INT NOT NULL,
	price DOUBLE,
	FOREIGN KEY (store_name, store_street, store_zip) REFERENCES store(s_name, street, zip)
);

CREATE TABLE customer(
	customerID INT PRIMARY KEY,
	first_name VARCHAR NOT NULL,
	last_name VARCHAR NOT NULL,
	bank_account VARCHAR,
	delivery_address VARCHAR
);

CREATE TABLE sale(
	customer INT REFERENCES customer(customerID),
    product INT REFERENCES product(prod_number),
	dateTime TIMESTAMP,
	delivery_address VARCHAR NOT NULL,
	bank_account VARCHAR NOT NULL,
	PRIMARY KEY (customer, product, dateTime)
);

CREATE TABLE rating(
	customer INT REFERENCES customer(customerID),
    product INT REFERENCES product(prod_number),
	stars INT NOT NULL,
	review TEXT,
	PRIMARY KEY (customer, product)
);

CREATE TABLE similar_products(
	product1 INT REFERENCES product(prod_number),
    product2 INT REFERENCES product(prod_number),
	common_category_count INT NOT NULL,
	PRIMARY KEY (product1, product2),
	CHECK (product1 < product2)
);


CREATE OR REPLACE FUNCTION calculate_avg_rating() RETURNS TRIGGER AS $BODY$
	BEGIN
		UPDATE product
		SET rating = (SELECT AVG(stars)
					FROM rating
					WHERE product=NEW.product)
		WHERE product=NEW.product;
		RETURN NEW;
	END;
$BODY$ LANGUAGE plpgsql;

CREATE TRIGGER calculate_avg_rating
	AFTER INSERT OR UPDATE ON rating
	FOR EACH ROW
	EXECUTE PROCEDURE calculate_avg_rating();

CREATE OR REPLACE FUNCTION get_common_cat_count(product1 INT, product2 INT)
	RETURNS INT AS $fun$
	DECLARE passed BOOLEAN;
	BEGIN
		SELECT COUNT(*) INTO passed
		FROM ((SELECT catID
			 FROM product_in_category
			 WHERE product=product1)
			 INTERSECT
			  (SELECT catID
			 FROM product_in_category
			 WHERE prodID=product2)
			 );
		RETURN passed;
	END;
$fun$  LANGUAGE plpgsql

	
CREATE OR REPLACE FUNCTION update_similar_products() RETURNS TRIGGER AS $BODY$
	DECLARE
		t_curs CURSOR FOR
			SELECT * FROM similar_products;
		t_row similar_products%rowtype;
	BEGIN
		FOR t_row IN t_curs LOOP
			UPDATE similar_products
			SET common_category_count = (CALL get_common_cat_count(t_row.product1, t_row.product2))
			WHERE CURRENT OF t_curs;
		END LOOP;
		RETURN NEW;
	END;
$BODY$ LANGUAGE plpgsql;

CREATE TRIGGER update_similar_products
	AFTER INSERT OR UPDATE ON product_in_category
	FOR EACH ROW
	EXECUTE PROCEDURE update_similar_products();