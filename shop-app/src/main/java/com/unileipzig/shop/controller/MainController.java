package com.unileipzig.shop.controller;

import com.unileipzig.shop.HibernateConnector;
import com.unileipzig.shop.InputException;
import com.unileipzig.shop.model.Customer;
import com.unileipzig.shop.model.Offer;
import com.unileipzig.shop.model.Product;
import com.unileipzig.shop.model.Review;
import com.unileipzig.shop.repository.OfferRepository;
import com.unileipzig.shop.repository.ProductRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        ProductRepository prodRepo = new ProductRepository();
        return prodRepo.getProductDetails(prodNumber);
    }

    @Override
    public List<Product> getProducts(String titlePattern) {
        ProductRepository prodRepo = new ProductRepository();
        return prodRepo.getProducts(titlePattern);
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
        ProductRepository prodRepo = new ProductRepository();
        return prodRepo.getTopProducts(number);
    }

    @Override
    public List<Product> getSimilarCheaperProducts(String prodNumber) throws InputException {
        ProductRepository prodRepo = new ProductRepository();
        List<Product> similarProducts = prodRepo.getSimilarProducts(prodNumber);
        List<Product> cheaperProducts = prodRepo.getCheaperProducts(prodNumber);

        if (cheaperProducts == null) {
            throw new InputException("Product " + prodNumber + " is not available in any store");
        }

        List<Product> similarAndCheaperProducts = similarProducts.stream()
                .filter(cheaperProducts::contains)
                .collect(Collectors.toList());

        return similarAndCheaperProducts;
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
        OfferRepository offerRepo = new OfferRepository();
        return offerRepo.getOffers(prodNumber);
    }
}
