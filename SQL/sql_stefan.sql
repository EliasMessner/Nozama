CREATE TABLE product(
    prod_number INT PRIMARY KEY,
    title VARCHAR NOT NULL,
    rating SMALLINT NOT NULL,
    sales_rank INT NOT NULL,
    image BLOB
)

CREATE TABLE book(
    prod_number INT PRIMARY KEY REF product(prod_number),
    author INT REF person(id),
    page_number INT,
    publication_date DATE,
    isbn BIGINT UNSIGNED UNIQUE,
    publisher VARCHAR
)

CREATE TABLE dvd(
    prod_number INT PRIMARY KEY REF product(prod_number),
    format VARCHAR,
    duration_minutes INT,
    region_code SMALLINT
)

CREATE TABLE music_cd(
    prod_number INT PRIMARY KEY REF product(prod_number),
    label VARCHAR,
    publication_date DATE,
    titles VARCHAR ARRAY,
)

CREATE TABLE dvd_person(
    dvd INT REF dvd(prod),
    person INT REF person(id),
    role VARCHAR,
    PRIMARY KEY(dvd, person, role)
)

CREATE TABLE cd_artist(
    cd INT REF cd(prod_number),
    artist INT REF person(id),
    PRIMARY KEY(cd, artist)
)

CREATE TABLE person(
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR,
    last_name VARCHAR
)