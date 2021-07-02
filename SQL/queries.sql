-- task 1
SELECT (SELECT COUNT(*)
        FROM music_cd) music_cd_count,
       (SELECT COUNT(*)
        FROM dvd) dvd_count,
       (SELECT COUNT(*)
        FROM book) book_count;

-- task 2
SELECT *
FROM (
         (SELECT 'book' AS type, prod_number, rating
          FROM product
                   NATURAL JOIN book
          ORDER BY rating DESC
          LIMIT 5)
         UNION
         (SELECT 'music_cd' AS type, prod_number, rating
          FROM product
                   NATURAL JOIN music_cd
          ORDER BY rating DESC
          LIMIT 5)
         UNION
         (SELECT 'dvd' AS type, prod_number, rating
          FROM product
                   NATURAL JOIN dvd
          ORDER BY rating DESC
          LIMIT 5)
     ) AS products
ORDER BY type ASC, rating DESC;

-- task 3
SELECT *
FROM product
WHERE prod_number
NOT IN (SELECT product
    FROM store_inventory
    WHERE price IS NOT NULL);

-- task 4
SELECT DISTINCT product
FROM store_inventory AS inv1
GROUP BY product
HAVING MAX(price)/2 >
       (SELECT price
        FROM store_inventory AS inv2
        WHERE inv1.product = inv2.product AND inv2.price IS NOT NULL
        ORDER BY price ASC
        LIMIT 1);

-- task 5
SELECT *
FROM product p
WHERE EXISTS (
    SELECT *
    FROM review
    WHERE product = p.prod_number
    AND stars = 1
    )
AND EXISTS (
    SELECT *
    FROM review
    WHERE product = p.prod_number
    AND stars = 5
    );

-- task 6
SELECT prod_number
FROM product
WHERE NOT EXISTS
    (SELECT *
    FROM review
    WHERE review.product = product.prod_number);

-- task 7
SELECT *
FROM customer c
WHERE 10 <= (
    SELECT COUNT(*)
    FROM review
    WHERE customer = c.username
          );

-- task 8
SELECT DISTINCT name
FROM person
WHERE person.id IN (
    (SELECT DISTINCT author
     FROM book_author JOIN dvd_person ON author = person)
    UNION
    (SELECT DISTINCT author
     FROM book_author JOIN cd_artist ON author = artist)
)
ORDER BY name ASC;

-- task 9
SELECT AVG(card)
FROM (
    SELECT cardinality(titles) card
    FROM music_cd
    ) cardinalities;

-- task 10
WITH RECURSIVE main_category_mapping (sub_category, main_category) AS
                   (SELECT sub_category, super_category
                    FROM category_hierarchy
                    UNION
                    SELECT mapping.sub_category, hierarchy.super_category
                    FROM main_category_mapping AS mapping, category_hierarchy AS hierarchy
                    WHERE mapping.main_category = hierarchy.sub_category)
SELECT DISTINCT pc1.product
FROM product_category AS pc1 JOIN main_category_mapping AS mc1 ON pc1.category = mc1.sub_category
WHERE EXISTS
          (SELECT *
           FROM similar_products AS sp JOIN product_category AS pc2 ON pc2.product = sp.product2 JOIN main_category_mapping AS mc2
                                                                                                      ON pc2.category = mc2.sub_category
           WHERE sp.product1 = pc1.product AND mc1.main_category != mc2.main_category);

-- task 11
SELECT *
FROM product p
WHERE NOT EXISTS(
    SELECT *
    FROM store s
    WHERE p.prod_number
    NOT IN (
        SELECT product
        FROM store_inventory
        WHERE store_name = s.s_name AND store_street = s.street AND store_zip = s.zip
        AND price IS NOT NULL
              )
    );

-- task 12
WITH
    leipzig_and_dresden AS (SELECT *
                            FROM product p
                            WHERE NOT EXISTS(
                                    SELECT *
                                    FROM store s
                                    WHERE p.prod_number
                                              NOT IN (
                                              SELECT product
                                              FROM store_inventory
                                              WHERE store_name = s.s_name AND store_street = s.street AND store_zip = s.zip
                                                AND price IS NOT NULL
                                          )
                                )),
    leipzig_and_dresden_count AS (SELECT COUNT(*) AS product_count FROM leipzig_and_dresden),
    leipzig_cheapest AS (SELECT COUNT(*) AS leipzig_cheapest_count
                        FROM leipzig_and_dresden WHERE (
                            (SELECT MIN(price)
                            FROM store_inventory si
                            WHERE si.product = leipzig_and_dresden.prod_number
                            AND si.store_name = 'Leipzig')
                            <
                            (SELECT MIN(price)
                            FROM store_inventory si
                            WHERE si.product = leipzig_and_dresden.prod_number
                            AND si.store_name = 'Dresden')))
SELECT CAST(leipzig_cheapest.leipzig_cheapest_count AS float) / leipzig_and_dresden_count.product_count
FROM leipzig_cheapest, leipzig_and_dresden_count;