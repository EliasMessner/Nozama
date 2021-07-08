CREATE TABLE store(
	s_name VARCHAR,
	street VARCHAR,
    zip CHAR(5),
	PRIMARY KEY(s_name, street, zip)
);

CREATE TABLE store_inventory(
	artID SERIAL PRIMARY KEY,
    product VARCHAR REFERENCES product(prod_number),
	store_name VARCHAR,
	store_street VARCHAR,
	store_zip CHAR(5),
	article_condition VARCHAR NOT NULL,
	price DECIMAL(10,2) CHECK (price IS NULL OR price >= 0),
	FOREIGN KEY (store_name, store_street, store_zip) REFERENCES store(s_name, street, zip)
);

CREATE INDEX ON store_inventory (product);

CREATE TABLE customer(
	username VARCHAR PRIMARY KEY,
	first_name VARCHAR,
	last_name VARCHAR,
	bank_account VARCHAR,
	street VARCHAR,
	zip CHAR(5)
);

CREATE TABLE sale(
	customer VARCHAR REFERENCES customer(username),
    product VARCHAR REFERENCES product(prod_number),
	dateTime TIMESTAMP CHECK(dateTime <= CURRENT_TIMESTAMP),
	delivery_address VARCHAR NOT NULL,
	bank_account VARCHAR NOT NULL,
	PRIMARY KEY (customer, product, dateTime)
);

CREATE TABLE review(
    id SERIAL PRIMARY KEY,
	customer VARCHAR REFERENCES customer(username),
    product VARCHAR REFERENCES product(prod_number),
    date DATE CHECK(date <= CURRENT_DATE),
	stars INT NOT NULL,
	summary VARCHAR,
	details TEXT
);

CREATE INDEX ON review (product);

CREATE TABLE similar_products(
	product1 VARCHAR REFERENCES product(prod_number),
    product2 VARCHAR REFERENCES product(prod_number),
	PRIMARY KEY (product1, product2)
);


CREATE OR REPLACE FUNCTION calculate_avg_rating() RETURNS TRIGGER AS $BODY$
	BEGIN
		UPDATE product
		SET rating = (SELECT AVG(stars)
					FROM review
					WHERE review.product=NEW.product)
		WHERE prod_number=NEW.product;
		RETURN NEW;
	END;
$BODY$ LANGUAGE plpgsql;

CREATE TRIGGER calculate_avg_rating
	AFTER INSERT OR UPDATE ON review
	FOR EACH ROW
	EXECUTE PROCEDURE calculate_avg_rating();


CREATE OR REPLACE FUNCTION check_exists_as_subtype() RETURNS TRIGGER AS $BODY$
BEGIN
    IF new.prod_number IN (
        SELECT prod_number FROM music_cd
        UNION SELECT prod_number FROM dvd
        UNION SELECT prod_number FROM book) THEN
        RETURN NEW;
    ELSE
        RAISE EXCEPTION 'Product (%) must exists as subtype', new.prod_number;
    END IF;
END;
$BODY$ LANGUAGE plpgsql;

CREATE CONSTRAINT TRIGGER check_exists_as_subtype
    AFTER INSERT OR UPDATE
    ON product
    DEFERRABLE INITIALLY DEFERRED
    FOR EACH ROW
EXECUTE PROCEDURE check_exists_as_subtype();

CREATE INDEX ON review (customer);

CREATE INDEX ON store_inventory (store_name, store_street, store_zip);