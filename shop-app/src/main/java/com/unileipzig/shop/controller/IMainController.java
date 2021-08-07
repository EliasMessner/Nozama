package com.unileipzig.shop.controller;

import com.unileipzig.shop.exceptions.InputException;
import com.unileipzig.shop.exceptions.AmbiguousCategoryNameException;
import com.unileipzig.shop.exceptions.CategoryNotFoundException;
import com.unileipzig.shop.model.*;

import java.util.List;

public interface IMainController {

    public void init();

    public void finish();

    public Product getProduct(String prodNumber);

    public List<Product> getProducts(String titlePattern);

    public Category getCategoryTree();

    public List<Product> getProductsByCategoryPath(String categoryPath) throws CategoryNotFoundException, AmbiguousCategoryNameException;

    public List<Product> getTopProducts(int number);

    public List<Product> getSimilarCheaperProducts(String prodNumber) throws InputException;

    public void addNewReview(String customer, String product, int stars, String summary, String details);

    public List<Customer> getTrolls(double ratingLimit);

    public List<Offer> getOffers(String prodNumber);
}
