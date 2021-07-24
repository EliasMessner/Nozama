package com.unileipzig.shop.repository;

import com.unileipzig.shop.HibernateConnector;
import com.unileipzig.shop.model.Book;
import com.unileipzig.shop.model.Dvd;
import com.unileipzig.shop.model.MusicCd;
import com.unileipzig.shop.model.Product;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.List;

public class ProductRepository {

    public Product getProduct(String prodNumber) {
        Session session = HibernateConnector.getSession();
        return session.get(Product.class, prodNumber);
    }

    public Product getProductDetails(String prodNumber) {
        Session session = HibernateConnector.getSession();
        Product product = session.get(Book.class, prodNumber);
        if (product == null) {
            product = session.get(Dvd.class, prodNumber);
        }
        if (product == null) {
            product = session.get(MusicCd.class, prodNumber);
        }
        return product;
    }

    public List<Product> getProducts(String titlePattern) {
        Session session = HibernateConnector.getSession();
        Query<Product> query = session.createQuery("FROM Product WHERE title LIKE :pattern", Product.class);
        query.setParameter("pattern", titlePattern);
        return query.list();
    }

    public List<Product> getTopProducts(int number) {
        Session session = HibernateConnector.getSession();
        Query<Product> query = session.createQuery(
                "SELECT p " +
                "FROM Product p " +
                "ORDER BY p.rating DESC, p.salesRank ASC, p.prodNumber ASC", Product.class);
        query.setMaxResults(number);
        return query.list();
    }

    public List<Product> getSimilarProducts(String prodNumber) {
        Session session = HibernateConnector.getSession();
        Query<Product> query = session.createQuery("SELECT sp FROM Product p INNER JOIN p.similarProducts sp WHERE " +
                "p.prodNumber = :givenProdNumber", Product.class);
        query.setParameter("givenProdNumber", prodNumber);
        return query.list();
    }

    public List<Product> getCheaperProducts(String prodNumber) {
        Product givenProduct = this.getProduct(prodNumber);
        Session session = HibernateConnector.getSession();
        Query<BigDecimal> queryMin = session.createQuery("SELECT MIN(price) FROM Offer o WHERE o.product = :givenProduct",
                BigDecimal.class);
        queryMin.setParameter("givenProduct", givenProduct);
        BigDecimal minPrice = queryMin.getSingleResult();
        if (minPrice == null) {
            return null;
        } else {
            Query<Product> queryCheaper = session.createQuery("SELECT DISTINCT p FROM Offer o INNER JOIN o.product p " +
                    "WHERE o.price < :minPrice", Product.class);
            queryCheaper.setParameter("minPrice", minPrice);
            return queryCheaper.list();
        }
    }

    public List<Product> getProductsByCategoryId(long catID) {
        Session session = HibernateConnector.getSession();
        Query<Product> query = session.createQuery("SELECT p FROM Category c INNER JOIN c.products p " +
                "WHERE c.id = :catID", Product.class);
        query.setParameter("catID", catID);
        return query.list();
    }
}
