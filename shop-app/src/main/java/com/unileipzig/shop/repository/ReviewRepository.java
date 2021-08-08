package com.unileipzig.shop.repository;

import com.unileipzig.shop.HibernateConnector;
import com.unileipzig.shop.model.Review;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ReviewRepository {

    public Review getReview(int serial) {
        Session session = HibernateConnector.getSession();
        return session.get(Review.class, serial);
    }

    public List<Review> getReviews() {
        Session session = HibernateConnector.getSession();
        Query<Review> query = session.createQuery("FROM Review", Review.class);
        return query.list();
    }

    public List<Review> getReviewsByUser(String username) {
        Session session = HibernateConnector.getSession();
        Query<Review> query = session.createQuery("SELECT DISTINCT r FROM Review r INNER JOIN r.customer c WHERE c.username = :username", Review.class);
        query.setParameter("username", username);
        return query.list();
    }

    public List<Review> getReviewsByProduct(String prodNumber) {
        Session session = HibernateConnector.getSession();
        Query<Review> query = session.createQuery("SELECT DISTINCT r FROM Review r INNER JOIN r.product p WHERE p.prodNumber = :prodNumber", Review.class);
        query.setParameter("prodNumber", prodNumber);
        return query.list();
    }

    public void addNewReview(Review review) {
        Session session = HibernateConnector.getSession();
        session.beginTransaction();
        session.save(review);
        session.getTransaction().commit();
    }
}
