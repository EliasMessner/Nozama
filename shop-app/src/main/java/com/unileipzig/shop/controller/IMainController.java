package com.unileipzig.shop.controller;

import com.unileipzig.shop.model.Customer;
import com.unileipzig.shop.model.Offer;
import com.unileipzig.shop.model.Product;
import com.unileipzig.shop.model.Review;

import java.util.List;

public interface IMainController {

    public void init() throws ClassNotFoundException;

    public void finish();

    public Product getProduct(String prodNumber);

    public List<Product> getProducts(String pattern);

    public String getCategoryTree();

    public List<Product> getProductsByCategoryPath(String categoryPath);

    public List<Product> getTopProducts(int number);

    public List<Product> getSimilarCheaperProduct(String prodNumber);

    public void addNewReview(String customer, String product, String rating, int stars, String summary, String details);

    public List<Review> getReviews(String product);

    public List<Customer> getTrolls(int ratingLimit);

    public List<Offer> getOffers(String prodNumber);
}
