CREATE TABLE person(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR,
    last_name VARCHAR
);

CREATE TABLE product(
    prod_number INT PRIMARY KEY,
    title VARCHAR NOT NULL,
    rating DOUBLE PRECISION NOT NULL,
    sales_rank INT NOT NULL,
    image VARCHAR
);

CREATE TABLE book(
    prod_number INT PRIMARY KEY REFERENCES product(prod_number),
    page_number INT,
    publication_date DATE,
    isbn BIGINT UNIQUE CHECK (isbn > 0),
    publishers VARCHAR ARRAY
);

CREATE TABLE dvd(
    prod_number INT PRIMARY KEY REFERENCES product(prod_number),
    format VARCHAR,
    duration_minutes INT,
    region_code SMALLINT
);

CREATE TABLE music_cd(
    prod_number INT PRIMARY KEY REFERENCES product(prod_number),
    labels VARCHAR ARRAY,
    publication_date DATE,
    titles VARCHAR ARRAY
);

CREATE TABLE book_author(
    book INT REFERENCES book(prod_number),
    author INT REFERENCES person(id),
    PRIMARY KEY(book, author)
)

CREATE TABLE dvd_person(
    dvd INT REFERENCES dvd(prod_number),
    person INT REFERENCES person(id),
    role VARCHAR,
    PRIMARY KEY(dvd, person, role)
);

CREATE TABLE cd_artist(
    cd INT REFERENCES music_cd(prod_number),
    artist INT REFERENCES person(id),
    PRIMARY KEY(cd, artist)
);

CREATE TABLE category(
    name VARCHAR PRIMARY KEY
);

CREATE TABLE category_hierarchy(
    super_category VARCHAR REFERENCES category(name),
    sub_category VARCHAR REFERENCES category(name),
    PRIMARY KEY(super_category, sub_category)
);

CREATE TABLE product_category(
    product INT REFERENCES product(prod_number),
    category VARCHAR REFERENCES category(name),
    PRIMARY KEY(product,category)
);