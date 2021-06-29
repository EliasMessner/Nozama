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

-- task 6
SELECT prod_number
FROM product
WHERE NOT EXISTS
    (SELECT *
    FROM review
    WHERE review.product = product.prod_number);

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

-- task 10
WITH RECURSIVE main_category_mapping (sub_category, main_category, level) AS
                   (SELECT sub_category, super_category, 1
                    FROM category_hierarchy
                    UNION
                    SELECT mapping.sub_category, hierarchy.super_category, mapping.level + 1
                    FROM main_category_mapping AS mapping, category_hierarchy AS hierarchy
                    WHERE mapping.main_category = hierarchy.sub_category)
SELECT DISTINCT pc1.product
FROM product_category AS pc1 JOIN main_category_mapping AS mc1 ON pc1.category = mc1.sub_category
WHERE EXISTS
          (SELECT *
           FROM similar_products AS sp JOIN product_category AS pc2 ON pc2.product = sp.product2 JOIN main_category_mapping AS mc2
                                                                                                      ON pc2.category = mc2.sub_category
           WHERE sp.product1 = pc1.product AND mc1.main_category != mc2.main_category);

-- task 12
-- leipzig_and_dresden assignment needs to be replaced by solution of task 11
WITH
    leipzig_and_dresden AS (SELECT prod_number FROM product),
    leipzig_and_dresden_count AS (SELECT COUNT(*) AS product_count FROM leipzig_and_dresden),
    leipzig_cheapest AS (SELECT COUNT(*) AS leipzig_cheapest_count
                         FROM store_inventory AS si1
                         WHERE si1.store_name = 'Leipzig'
                           AND si1.product IN (SELECT prod_number FROM leipzig_and_dresden)
                           AND si1.price = (SELECT MIN(price) FROM store_inventory AS si2 WHERE si2.product = si1.product))
SELECT CAST(leipzig_cheapest.leipzig_cheapest_count AS float) / leipzig_and_dresden_count.product_count AS ratio
FROM leipzig_cheapest, leipzig_and_dresden_count


