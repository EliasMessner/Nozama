CREATE TABLE person(
    id SERIAL PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE product(
    prod_number VARCHAR PRIMARY KEY,
    title VARCHAR NOT NULL,
    rating DOUBLE PRECISION NOT NULL,
    sales_rank INT NOT NULL,
    image VARCHAR
);

CREATE TABLE book(
    prod_number VARCHAR PRIMARY KEY REFERENCES product(prod_number),
    page_number INT CHECK(page_number IS NULL OR page_number > 0),
    publication_date DATE,
    isbn VARCHAR UNIQUE,
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

CREATE TABLE dvd_person(
    dvd VARCHAR REFERENCES dvd(prod_number),
    person INT REFERENCES person(id),
    role VARCHAR,
    PRIMARY KEY(dvd, person, role)
);

CREATE TABLE cd_artist(
    cd VARCHAR REFERENCES music_cd(prod_number),
    artist INT REFERENCES person(id),
    PRIMARY KEY(cd, artist)
);

CREATE TABLE category(
    id SERIAL PRIMARY KEY,
    name VARCHAR
);

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