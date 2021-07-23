package com.unileipzig.shop.controller;

import com.unileipzig.shop.HibernateConnector;
import com.unileipzig.shop.InputException;
import com.unileipzig.shop.exceptions.AmbiguousCategoryNameException;
import com.unileipzig.shop.exceptions.CategoryNotFoundException;
import com.unileipzig.shop.model.*;
import com.unileipzig.shop.repository.*;
import org.hibernate.Session;

import java.util.*;
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
    public List<Product> getProductsByCategoryPath(String categoryPath) throws CategoryNotFoundException, AmbiguousCategoryNameException {
        categoryPath = categoryPath.replaceAll("[\\s\\r]+", "");
        String[] path = Arrays.stream(categoryPath.split("/")).filter(s -> s.length() != 0).toArray(String[]::new);
        if (path.length == 0) {
            throw new CategoryNotFoundException(categoryPath);
        }
        Category category = findCategoryRecursive(new CategoryRepository().getMainCategories(), 0, path);
        return new ProductRepository().getProductsByCategoryId(category.getId());
    }

    private Category findCategoryRecursive(Collection<Category> categories, int index, String[] categoryPath) throws AmbiguousCategoryNameException, CategoryNotFoundException {
        List<Category> matches = categories.stream()
                .filter(category -> category.getName().replaceAll("[\\s\\r]+", "").equals(categoryPath[index])) // remove whitespaces and linebreaks because category names might be parsed incorrectly and have additional whitespaces etc.
                .collect(Collectors.toList());
        if (matches.isEmpty()) {
            throw new CategoryNotFoundException(categoryPath, index);
        }
        if (matches.size() > 1) {
            throw new AmbiguousCategoryNameException(categoryPath, index);
        }
        Category match = matches.get(0);
        if (index == categoryPath.length - 1) {
            return match;
        }
        return findCategoryRecursive(match.getChildren(), index+1, categoryPath);
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
    public List<Customer> getTrolls(double ratingLimit) {
        ReviewRepository reviewRepository = new ReviewRepository();
        CustomerRepository customerRepository = new CustomerRepository();
        List<Customer> customers = customerRepository.getCustomers();
        List<Customer> trolls = new ArrayList<>();
        if (customers == null) {
            return trolls;
        }
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
