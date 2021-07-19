package com.unileipzig.shop.controller;

import com.unileipzig.shop.HibernateConnector;
import com.unileipzig.shop.InputException;
import com.unileipzig.shop.model.*;
import com.unileipzig.shop.repository.*;
import org.hibernate.Session;

import java.util.ArrayList;
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
    public Category getCategoryTree() {
        CategoryRepository categoryRepository = new CategoryRepository();
        Category all = new Category("all");
        for (Category mainCategory : categoryRepository.getMainCategories()) {
            all.addChild(mainCategory);
        }
        return all;
    }

    @Override
    public List<Product> getProductsByCategoryPath(String categoryPath) {
        String[] path = categoryPath.split("/");
        Category category = new CategoryRepository().getMainCategories().stream()
                .filter(category1 -> category1.getName().equals(path[0]))
                .collect(Collectors.toList())
                .get(0);
        for (int i = 1; i < path.length; i++) {
            int finalI = i;
            category = category.getChildren().stream()
                    .filter(category2 -> category2.getName().equals(path[finalI]))
                    .collect(Collectors.toList())
                    .get(0);
        }
        return new ProductRepository().getProductsByCategoryId(category.getId());
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
    public void addNewReview(String username, String prodNumber, int stars, String summary, String details) {
        Session session = HibernateConnector.getSession();
        session.beginTransaction();
        Customer customer = new CustomerRepository().getCustomer(username);
        Product product = new ProductRepository().getProduct(prodNumber);
        Review review = new Review(customer, product, stars);
        review.setSummary(summary);
        review.setDetails(details);
        session.save(review);
        session.getTransaction().commit();
    }

    @Override
    public List<Review> getReviews(String product) {
        return null;
    }

    @Override
    public List<Customer> getTrolls(double ratingLimit) {
        ReviewRepository reviewRepository = new ReviewRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        List<Customer> customers = customerRepository.getCustomers();
        List<Customer> trolls = new ArrayList<>();
        for (Customer c : customers) {
            if (reviewRepository.getReviews(c.getUsername()).stream()
                    .mapToInt(Review::getStars)
                    .average().orElse(5.0) < ratingLimit) {
                trolls.add(c);
            }
        }
        return trolls;
    }

    @Override
    public List<Offer> getOffers(String prodNumber) {
        OfferRepository offerRepo = new OfferRepository();
        return offerRepo.getOffers(prodNumber);
    }
}
