package com.unileipzig.shop.controller;

import com.unileipzig.shop.InputException;
import com.unileipzig.shop.model.*;

import java.util.List;

public interface IMainController {

    public void init() throws ClassNotFoundException;

    public void finish();

    public Product getProduct(String prodNumber);

    public List<Product> getProducts(String titlePattern);

    public Category getCategoryTree();

    public List<Product> getProductsByCategoryPath(String categoryPath);

    public List<Product> getTopProducts(int number);

    public List<Product> getSimilarCheaperProducts(String prodNumber) throws InputException;

    public void addNewReview(String customer, String product, int stars, String summary, String details);

    public List<Review> getReviews(String product);

    public List<Customer> getTrolls(double ratingLimit);

    public List<Offer> getOffers(String prodNumber);
}
