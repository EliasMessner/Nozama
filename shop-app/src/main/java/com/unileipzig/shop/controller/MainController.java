package com.unileipzig.shop.controller;

import com.unileipzig.shop.HibernateConnector;
import com.unileipzig.shop.model.Customer;
import com.unileipzig.shop.model.Offer;
import com.unileipzig.shop.model.Product;
import com.unileipzig.shop.model.Review;

import java.util.List;

public class MainController implements IMainController {

    @Override
    public void init() throws ClassNotFoundException {
        HibernateConnector.initSessionFactory();
    }

    @Override
    public void finish() {
        HibernateConnector.finishSessionFactory();
    }

    @Override
    public Product getProduct(String prodNumber) {
        return null;
    }

    @Override
    public List<Product> getProducts(String pattern) {
        return null;
    }

    @Override
    public String getCategoryTree() {
        return null;
    }

    @Override
    public List<Product> getProductsByCategoryPath(String categoryPath) {
        return null;
    }

    @Override
    public List<Product> getTopProducts(int number) {
        return null;
    }

    @Override
    public List<Product> getSimilarCheaperProduct(String prodNumber) {
        return null;
    }

    @Override
    public void addNewReview(String customer, String product, String rating, int stars, String summary, String details) {

    }

    @Override
    public List<Review> getReviews(String product) {
        return null;
    }

    @Override
    public List<Customer> getTrolls(int ratingLimit) {
        return null;
    }

    @Override
    public List<Offer> getOffers(String prodNumber) {
        return null;
    }
}
