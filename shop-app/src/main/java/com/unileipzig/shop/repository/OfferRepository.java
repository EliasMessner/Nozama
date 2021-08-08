package com.unileipzig.shop.repository;

import com.unileipzig.shop.HibernateConnector;
import com.unileipzig.shop.model.Offer;
import com.unileipzig.shop.model.Product;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class OfferRepository {

    public List<Offer> getOffers(String prodNumber) {
        ProductRepository prodRepo = new ProductRepository();
        Product givenProduct = prodRepo.getProduct(prodNumber);
        Session session = HibernateConnector.getSession();
        Query<Offer> query = session.createQuery("FROM Offer o WHERE o.product = :givenProduct", Offer.class);
        query.setParameter("givenProduct", givenProduct);
        return query.list();
    }
}
