CREATE TABLE person(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE INDEX ON person (name);

CREATE TABLE product(
    prod_number VARCHAR PRIMARY KEY,
    title VARCHAR NOT NULL,
    rating DOUBLE PRECISION NOT NULL DEFAULT 3,
    sales_rank INT,
    image VARCHAR
);

CREATE INDEX ON product (title);

CREATE TABLE book(
    prod_number VARCHAR PRIMARY KEY REFERENCES product(prod_number),
    page_number INT CHECK(page_number IS NULL OR page_number > 0),
    publication_date DATE,
    isbn VARCHAR UNIQUE CHECK(length(isbn) = 10 OR length(isbn) = 13),
    publishers VARCHAR ARRAY
);

CREATE TABLE dvd(
    prod_number VARCHAR PRIMARY KEY REFERENCES product(prod_number),
    format VARCHAR,
    duration_minutes INT CHECK(duration_minutes IS NULL OR duration_minutes > 0),
    region_code SMALLINT CHECK(region_code BETWEEN 0 AND 8)
);

CREATE TABLE music_cd(
    prod_number VARCHAR PRIMARY KEY REFERENCES product(prod_number),
    labels VARCHAR ARRAY,
    publication_date DATE,
    titles VARCHAR ARRAY
);

CREATE TABLE book_author(
    book VARCHAR REFERENCES book(prod_number),
    author INT REFERENCES person(id),
    PRIMARY KEY(book, author)
);

CREATE TABLE dvd_actor(
    dvd VARCHAR REFERENCES dvd(prod_number),
    actor INT REFERENCES person(id),
    PRIMARY KEY(dvd, actor)
);

CREATE TABLE dvd_creator(
    dvd VARCHAR REFERENCES dvd(prod_number),
    creator INT REFERENCES person(id),
    PRIMARY KEY(dvd, creator)
);

CREATE TABLE dvd_director(
    dvd VARCHAR REFERENCES dvd(prod_number),
    director INT REFERENCES person(id),
    PRIMARY KEY(dvd, director)
);

CREATE TABLE cd_artist(
    cd VARCHAR REFERENCES music_cd(prod_number),
    artist INT REFERENCES person(id),
    PRIMARY KEY(cd, artist)
);

CREATE TABLE category(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL
);

CREATE INDEX ON category (name);

CREATE TABLE category_hierarchy(
    super_category INT REFERENCES category(id),
    sub_category INT REFERENCES category(id),
    PRIMARY KEY(super_category, sub_category)
);

CREATE TABLE product_category(
    product VARCHAR REFERENCES product(prod_number),
    category INT REFERENCES category(id),
    PRIMARY KEY(product,category)
);

CREATE OR REPLACE FUNCTION check_one_artist() RETURNS TRIGGER AS $BODY$
DECLARE
selected_cd music_cd%rowtype;
BEGIN
SELECT cd INTO selected_cd FROM cd_artist WHERE cd = NEW.prod_number LIMIT 1;
IF NOT FOUND THEN RAISE EXCEPTION 'At least one artist required for cd %', NEW.prod_number; END IF;
RETURN NULL;
END;
$BODY$ LANGUAGE plpgsql;

CREATE CONSTRAINT TRIGGER one_artist
    AFTER INSERT ON music_cd
    INITIALLY DEFERRED
    FOR EACH ROW
    EXECUTE FUNCTION check_one_artist();