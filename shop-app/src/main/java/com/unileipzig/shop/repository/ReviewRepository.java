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

    public List<Review> getReviews(String username) {
        Session session = HibernateConnector.getSession();
        Query<Review> query = session.createQuery("FROM Review r WHERE r.customer.username = :username", Review.class);
        query.setParameter("username", username);
        return query.list();
    }
}
